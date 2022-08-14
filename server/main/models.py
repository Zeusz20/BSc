from django.contrib.auth.models import User
from django.db.models import *
from datetime import datetime
from hashlib import sha256


def check_file(model, filename):
    # validate file
    from os.path import splitext
    if model.file is None or splitext(model.file.name)[1] != '.gwp':
        raise FileNotFoundError

    # compare hash
    model.file.open()
    new_file_hash = sha256(model.file.read()).digest()

    for project in Project.objects.all():
        project.file.open()
        file_hash = sha256(project.file.read()).digest()
        if new_file_hash == file_hash:
            raise FileExistsError

    return filename


class Project(Model):
    user = ForeignKey(User, on_delete=CASCADE)
    author = CharField(max_length=128)
    name = CharField(max_length=128)
    description = TextField()
    file = FileField(upload_to=check_file)
    date_added = TimeField(default=datetime.today())
    deleted = BooleanField(default=False)

    def serialize(self):
        return {
            'id': self.id,
            'author': self.author,
            'name': self.name,
            'description': self.description,
            'file': self.file.name
        }
