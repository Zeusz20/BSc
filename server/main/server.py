import json, socket
from threading import Thread

SERVER_INFO = {
    'host': socket.gethostbyname(socket.gethostname()),
    'port': 5050,
    'buffer': 1024 * 8,  # 8kB
    'encoding': 'utf-8',
    'wait': 50,  # millis

    'create': '$_create',           # create new game
    'join': '$_join',               # connect to game
    'download': '$_download',       # joining player requested the project file
    'over': '$_over',               # host player has sent the project file
    'ready': '$_ready',             # player is ready to play
    'disconnect': '$_disconnect',   # disconnect from game
    'invalid': '$_invalid',         # invalid game id
    'ping': '$_ping',               # ping server
    'id_pattern': '^[A-Z0-9]{4}$',
}

_CLOSE_MESSAGES = (SERVER_INFO['disconnect'], '', None)
_FILE_TRANSFER_MESSAGES = (SERVER_INFO['download'], SERVER_INFO['over'])


class Server:

    @staticmethod
    def start():
        Thread(target=lambda: Server().listen(), daemon=True).start()

    def __init__(self):
        # contains game_id -> {host_address, join_address} mappings
        self._clients = dict()

        server_address = (SERVER_INFO['host'], SERVER_INFO['port'])
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(server_address)

    def listen(self):
        self.socket.listen()
        while True:
            connection, address = self.socket.accept()
            client_thread = Thread(target=self._handle_client, args=[connection, address], daemon=True)
            client_thread.start()

    def send(self, connection, address, data):
        message = (data + '\n').encode(SERVER_INFO['encoding'])
        print('[SEND]', address, message)
        connection.sendto(message, address)

    def receive(self, connection, address):
        message = connection.recv(SERVER_INFO['buffer']).decode(SERVER_INFO['encoding'])
        print('[RECV]', address, message)
        return message

    def close(self, storage):
        game_id = storage['game_id']
        player = 'host' if storage['is_host'] else 'join'
        other = 'join' if storage['is_host'] else 'host'

        self._clients[game_id][player]['connection'].close()
        self._clients[game_id][player]['connection'] = None
        self._clients[game_id][player]['address'] = None

        # disconnect other player if they're still connected
        if self._clients[game_id][other]['connection']:
            self.send(self._clients[game_id][other]['connection'], self._clients[game_id][other]['address'], SERVER_INFO['disconnect'])

        # destroy game if no player is connected
        if self._clients[game_id]['host']['connection'] is None and self._clients[game_id]['join']['connection'] is None:
            del self._clients[game_id]
            print(f"[GAME] Instance {game_id} destroyed")

    def _handle_client(self, connection, address):
        storage = {'game_id': None, 'is_host': None, 'file_transfer': False}
    
        while (message := self.receive(connection, address)) not in _CLOSE_MESSAGES:
            if message == SERVER_INFO['ping']:
                pass
            elif message == SERVER_INFO['create']:
                filename = self.receive(connection, address)
                self._create_game(storage, connection, address, filename)
            elif message == SERVER_INFO['join']:
                game_id = self.receive(connection, address)
                self._join_game(storage, connection, address, game_id)
            elif message == SERVER_INFO['ready']:
                self._communicate(storage, SERVER_INFO['ready'])
            elif message in _FILE_TRANSFER_MESSAGES:
                self._transfer_file(storage, message)
            else:
                self._communicate(storage, message)

        self.close(storage)

    def _create_game(self, storage, connection, address, filename):
        from string import ascii_uppercase, digits
        from random import choices

        game_id = ''.join(choices(ascii_uppercase + digits, k=4))
        self.send(connection, address, game_id)  # send back game id

        # create game and wait for other player
        storage['game_id'] = game_id
        storage['is_host'] = True

        self._clients[game_id] = {
            'host': {
                'connection': connection,
                'address': address
            },
            'join': {
                'connection': None,
                'address': None
            },
            'project': filename,
        }

    def _join_game(self, storage, connection, address, game_id):
        from re import match

        if not match(SERVER_INFO['id_pattern'], game_id) or self._clients.get(game_id) is None:
            self.send(connection, address, SERVER_INFO['invalid'])
        else:
            # establish connection
            self._clients[game_id]['join']['connection'] = connection
            self._clients[game_id]['join']['address'] = address
            
            # send to game data to join
            project = self._clients[game_id]['project']
            self.send(self._clients[game_id]['join']['connection'], self._clients[game_id]['join']['address'], game_id)
            self.send(self._clients[game_id]['join']['connection'], self._clients[game_id]['join']['address'], project)

            storage['game_id'] = game_id
            storage['is_host'] = False

    def _transfer_file(self, storage, message):
        if message == SERVER_INFO['download']:
            # joining player requests file transfer
            if not storage['is_host']:
                self._communicate(storage, message)
            storage['file_transfer'] = True
        elif message == SERVER_INFO['over']:
            # host player finished transferring file
            storage['file_transfer'] = False
            if storage['is_host']:
                self._communicate(storage, message)

    def _communicate(self, storage, message):
        other = 'join' if storage['is_host'] else 'host'

        self.send(
            self._clients[storage['game_id']][other]['connection'],
            self._clients[storage['game_id']][other]['address'],
            message
        )
