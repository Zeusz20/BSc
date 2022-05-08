# Localization implemented this way does NOT depend on browser language settings
# TODO: rework this

RECOVERY_MESSAGE = 0
RECOVERY_SUBJECT = 1
WRONG_LOGIN = 2
WRONG_REGISTER_USERNAME = 3
WRONG_REGISTER_EMAIL = 4
WRONG_REGISTER_PASSWORD = 5
SUCCESSFUL_REGISTRATION = 6
MAIL_SENT = 7
USER_DOES_NOT_EXIST = 8
SUCCESSFUL_EMAIL_CHANGE = 9
SUCCESSFUL_PASSWORD_CHANGE = 10
FAILED_PASSWORD_CHANGE = 11
FILE_UPLOAD = 12
FILE_UPDATE = 13
FILE_DELETE = 14
FILE_EXISTS = 15
INVALID_FILE = 16
GOOGLE_ACCOUNT_ASSOCIATION = 17


_MESSAGES = [
    ('Dear %s!\nYour new Cloud password is: %s\n\nCloud Support',
     'Kedves %s!\nAz új Felhő jelszava: %s\n\nFelhő Support'),

    ('Password Recovery', 'Jelszó Visszaállítás'),

    ('Wrong username or password', 'Hibás felhasználónév vagy jelszó'),

    ('Username already in use', 'A felhasználónév már használatban'),

    ('Email already in use', 'Az email cím már használatban'),

    ('Invalid passwords', 'Helytelen jelszavak'),

    ('Registration successful', 'Sikeres regisztráció'),

    ('We mailed your new password', 'Elküldtük emailben az új jelszavát'),

    ('Email is not registered', 'Az email cím nem regisztrált'),

    ('Email change successfully', 'Email sikeresen megváltoztatva'),

    ('Password changed successfully', 'Jelszó sikeresen megváltoztatva'),

    ('Unsuccessful password change', 'A jelszó megváltoztatása sikertelen'),

    ('Project uploaded successfully', 'Projekt sikeresen feltöltve'),

    ('Project updated successfully', 'Projekt sikeresen frissítve'),

    ('Project deleted successfully', 'Projekt sikeresen törölve'),

    ('Project already exists', 'A projekt már létezik'),

    ('Project is invalid', 'Érvénytelen projekt'),

    ('This email is associated with a Google account. Try loggin in with Google!',
     'Ez az email cím egy Google fiókhoz van kötve. Próbáljon belépni Google-n keresztül!'),
]


def localize(request, message_id):
    lang = request.session.get('lang', 'en')
    index = 1 if lang == 'hu' else 0
    return _MESSAGES[message_id][index]
