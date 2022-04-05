# Localization implemented this way does NOT depend on browser language settings

RECOVERY_MESSAGE = 0
RECOVERY_SUBJECT = 1
WRONG_LOGIN = 2
WRONG_REGISTER_USERNAME = 3
WRONG_REGISTER_PASSWORD = 4
SUCCESSFUL_REGISTRATION = 5
MAIL_SENT = 6
USER_DOES_NOT_EXIST = 7
SUCCESSFUL_EMAIL_CHANGE = 8
SUCCESSFUL_PASSWORD_CHANGE = 9
FAILED_PASSWORD_CHANGE = 10
FILE_UPLOAD = 11
FILE_UPDATE = 12
FILE_DELETE = 13
FILE_EXISTS = 14
INVALID_FILE = 15


_MESSAGES = [
    ('Dear %s!\nYour new Cloud password is: %s\n\nCloud Support',
     'Kedves %s!\nAz új Felhő jelszava: %s\n\nFelhő Support'),

    ('Password Recovery', 'Jelszó Visszaállítás'),

    ('Wrong username or password', 'Hibás felhasználónév vagy jelszó'),

    ('Username already in use', 'A felhasználónév már használatban'),

    ('Invalid passwords', 'Helytelen jelszavak'),

    ('Registration successful', 'Sikeres regisztráció'),

    ('We mailed your new password', 'Elküldtük emailben az új jelszavát'),

    ('User does not exist', 'A felhasználó nem létezik'),

    ('Email change successfully', 'Email sikeresen megváltoztatva'),

    ('Password changed successfully', 'Jelszó sikeresen megváltoztatva'),

    ('Unsuccessful password change', 'A jelszó megváltoztatása sikertelen'),

    ('Project uploaded successfully', 'Projekt sikeresen feltöltve'),

    ('Project updated successfully', 'Projekt sikeresen frissítve'),

    ('Project deleted successfully', 'Projekt sikeresen törölve'),

    ('Project already exists', 'A projekt már létezik'),

    ('Project is invalid', 'Érvénytelen projekt'),
]


def localize(request, message_id):
    lang = request.session.get('lang', 'en')
    index = 1 if lang == 'hu' else 0
    return _MESSAGES[message_id][index]
