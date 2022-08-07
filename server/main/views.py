from django.conf import settings
from django.contrib import messages
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.models import User
from django.db.transaction import atomic
from django.shortcuts import render
from django.views import View
from .localization import *
from .models import GWProject

# TODO
#   client to client
#   bsc home page


def redirect(root, **get_args):
    from django.shortcuts import redirect as django_redirect
    url = root + '?'
    for key, value in get_args.items():
        url += f'{key}={value}&'
    return django_redirect(url[:len(url) - 1])


# Create your views here
class IndexView(View):
    def get(self, request):
        from django.views.debug import default_urlconf
        return default_urlconf(request)


class ProjectView(View):
    def get(self, request):
        from django.http import Http404
        raise Http404

    def post(self, request):
        post_type = request.POST.get('type')
        try:
            if post_type == 'update':
                self._update_entry(request)
            elif post_type == 'delete':
                self._delete_entry(request)
            else:
                self._save_entry(request)
        except ImportError:
            messages.error(request, localize(request, INVALID_FILE))
        except FileExistsError:
            messages.error(request, localize(request, FILE_EXISTS))
        except (FileNotFoundError, GWProject.DoesNotExist):
            pass

        return redirect('/user/home/')

    @atomic
    def _save_entry(self, request):
        file = request.FILES.get('file')

        if file:
            project = GWProject(user=request.user, file=file)
            project.save()
            self._update_meta(project)
            messages.success(request, localize(request, FILE_UPLOAD))

    @atomic
    def _update_entry(self, request):
        project = GWProject.objects.select_for_update().get(id=request.POST.get('id', -1))
        old_file = project.file
        new_file = request.FILES.get('file')

        if new_file:
            project.file = new_file
            project.save()
            self._update_meta(project)
            self._remove_orphaned_files(old_file)
            messages.success(request, localize(request, FILE_UPDATE))

    @atomic
    def _delete_entry(self, request):
        project_id = request.POST.get('id')

        if project_id:
            project = GWProject.objects.select_for_update().get(id=project_id)
            self._remove_orphaned_files(project.file)
            project.delete()
            messages.success(request, localize(request, FILE_DELETE))

    def _update_meta(self, project):
        from javaobj import loads

        with open(project.file.path, 'rb') as file:
            jProject = loads(file.read())

        if hasattr(jProject, 'valid') and jProject.valid:
            project.author = jProject.author if jProject.author else '—'
            project.name = jProject.name if jProject.name else '—'
            project.description = jProject.description if jProject.description else '—'
            project.save()
        else:
            # file is uploaded before validity check;
            # delete invalid uploaded file
            self._remove_orphaned_files(project.file)
            raise ImportError

    def _remove_orphaned_files(self, file):
        from os import remove
        from os.path import join
        from threading import Thread

        # got PermissionError without thread
        Thread(target=lambda: remove(join(settings.MEDIA_ROOT, file.name))).start()


class UserView(View):
    view = None

    def get(self, request):
        authenticated = request.user.is_authenticated

        if self.view is None:
            request.session['lang'] = request.GET.get('lang', 'en')
            return redirect('/user/login/') if not authenticated else redirect('/user/home/', page='1')

        elif self.view in ['login', 'register', 'recovery']:
            return render(request, f'main/{self.view}.html') if not authenticated else redirect('/user/home/', page='1')

        elif self.view in ['home', 'search']:
            from django.core.paginator import Paginator
            from django.db.models import Q

            if authenticated:
                db_param = Q(user=request.user) if self.view == 'home' else ~Q(user=request.user)
                projects = GWProject.objects.filter(db_param)
            else:
                projects = GWProject.objects.all()

            query = request.GET.get('query')
            page = request.GET.get('page', '1')

            if query:
                projects = [project for project in projects
                            if query.lower()
                            in ' '.join((project.author.lower(), project.name.lower(), project.description.lower()))]

            projects = list(map(lambda project: project.serialize(), projects))
            return render(request, 'main/user.html', {'pages': Paginator(projects, 10).page(page)})

        elif self.view == 'settings':
            return render(request, 'main/settings.html')

        else:
            lang = request.session.get('lang', 'en')
            logout(request)
            return redirect('/user/', lang=lang)

    def post(self, request):
        post_type = request.POST.get('type')

        if post_type == 'login':
            return self._login(request)
        elif post_type == 'register':
            return self._register(request)
        elif post_type == 'recovery':
            return self._recovery(request)
        else:
            return self._settings(request)

    def _login(self, request):
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = authenticate(username=username, password=password)

        if user:
            login(request, user)
            return redirect('/user/home/')
        else:
            messages.error(request, localize(request, WRONG_LOGIN))
            return redirect('/user/login/')

    def _register(self, request):
        username = request.POST.get('username')
        email = request.POST.get('email')
        password1 = request.POST.get('password1')
        password2 = request.POST.get('password2')

        try:
            User.objects.get(username=username)
            messages.error(request, localize(request, WRONG_REGISTER_USERNAME))
            return redirect('/user/register/')
        except User.DoesNotExist:
            try:
                User.objects.get(email=email)
                messages.error(request, localize(request, WRONG_REGISTER_EMAIL))
                return redirect('/user/register/')
            except User.DoesNotExist:
                if password1 != password2:
                    messages.error(request, localize(request, WRONG_REGISTER_PASSWORD))
                    return redirect('/user/register/')
                else:
                    user = User(username=username, email=email)
                    user.set_password(password1)
                    user.save()
                    messages.success(request, localize(request, SUCCESSFUL_REGISTRATION))
                    return redirect('/user/login/')

    def _recovery(self, request):
        email = request.POST.get('email', '').strip()

        if email != '':
            try:
                from django.core.mail import send_mail
                from uuid import uuid4
                user = User.objects.get(email=email)

                # check if email is associated with a google account
                if user.username in map(lambda it: it.username, User.objects.all()):
                    messages.warning(request, localize(request, GOOGLE_ACCOUNT_ASSOCIATION))
                    return redirect('/user/login/')

                # generate new password
                new_password = str(uuid4()).replace('-', '')
                user.set_password(new_password)
                user.save()

                send_mail(
                    localize(request, RECOVERY_SUBJECT),
                    localize(request, RECOVERY_MESSAGE) % (user.username, new_password),
                    settings.EMAIL_HOST_USER,
                    [user.email]
                )

                messages.success(request, localize(request, MAIL_SENT))
                return redirect('/user/login/')
            except User.DoesNotExist:
                messages.error(request, localize(request, USER_DOES_NOT_EXIST))
                return render(request, 'main/recovery.html')

    def _settings(self, request):
        email_change = request.POST.get('type') == 'email'

        if email_change:
            request.user.email = request.POST.get('email')
            request.user.save()
            messages.success(request, localize(request, SUCCESSFUL_EMAIL_CHANGE))
            return redirect('/user')
        else:
            from django.contrib.auth.forms import PasswordChangeForm
            from django.contrib.auth import update_session_auth_hash

            form = PasswordChangeForm(request.user, request.POST)
            if form.is_valid():
                form.save()
                update_session_auth_hash(request, request.user)
                messages.success(request, localize(request, SUCCESSFUL_PASSWORD_CHANGE))
                return redirect('/user')
            else:
                messages.error(request, localize(request, FAILED_PASSWORD_CHANGE))
                return redirect('/user/settings/')


class GameView(View):

    def get(self, request):
        from django.http import JsonResponse
        from .server import SERVER_INFO

        return JsonResponse(SERVER_INFO)
