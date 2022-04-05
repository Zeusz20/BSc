from django.urls import path
from .views import *

urlpatterns = [
    path('', IndexView.as_view()),
    path('projects/', ProjectView.as_view()),

    path('user/', UserView.as_view(view=None)),
    path('user/login/', UserView.as_view(view='login')),
    path('user/register/', UserView.as_view(view='register')),
    path('user/recovery/', UserView.as_view(view='recovery')),
    path('user/home/', UserView.as_view(view='home')),
    path('user/search/', UserView.as_view(view='search')),
    path('user/settings/', UserView.as_view(view='settings')),
    path('user/logout/', UserView.as_view(view='logout')),
]
