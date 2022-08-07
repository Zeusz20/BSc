import socket
from threading import Thread

SERVER_INFO = {
    'host': socket.gethostbyname(socket.gethostname()),
    'port': 5050,
    'buffer': 1024,  # 1kB
    'format': 'utf-8',

    'create': '$_create',           # create new game
    'join': '$_join',               # connect to game
    'start': '$_start',             # players can communicate
    'disconnect': '$_disconnect',   # disconnect from game
    'id_pattern': '^[A-Z0-9]{4}$',
}


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
        message = (data + '\n').encode(SERVER_INFO['format'])
        connection.sendto(message, address)

    def receive(self, connection):
        return connection.recv(SERVER_INFO['buffer']).decode(SERVER_INFO['format']).strip()

    def _handle_client(self, connection, address):
        try:
            while (message := self.receive(connection)) != SERVER_INFO['disconnect']:
                if message == SERVER_INFO['create']:
                    self._create_game(connection, address)
                elif message == SERVER_INFO['join']:
                    game_id = self.receive(connection).upper()
                    self._join_game(connection, address, game_id)
                else:
                    self._communicate(message)
        except ValueError:
            pass

        connection.close()

    def _create_game(self, connection, address):
        from string import ascii_uppercase, digits
        from random import choices

        game_id = ''.join(choices(ascii_uppercase + digits, k=4))
        self.send(connection, address, game_id)  # send back game id

        # create game and wait for other player
        self._clients[game_id] = {
            'host': {
                'connection': connection,
                'address': address
            },
            'join': {
                'connection': None,
                'address': None
            }
        }

    def _join_game(self, connection, address, game_id):
        from re import match

        if not match(SERVER_INFO['id_pattern'], game_id):
            raise ValueError(f'Invalid game ID: {game_id}')

        if not self._clients.get(game_id):
            self.send(connection, address, "Game doesn't exist")
        else:
            # establish connection
            self._clients[game_id]['join']['connection'] = connection
            self._clients[game_id]['join']['address'] = address

            self.send(self._clients[game_id]['host']['connection'], self._clients[game_id]['host']['address'], SERVER_INFO['start'])
            self.send(self._clients[game_id]['join']['connection'], self._clients[game_id]['join']['address'], SERVER_INFO['start'])

    def _communicate(self, message):
        from json import JSONDecodeError, loads

        try:
            decoded = loads(message)
            game_id = decoded.get('game_id')
            is_host = decoded.get('is_host')
            client = 'host' if is_host else 'join'

            if self._clients.get(game_id):
                if decoded.get('disconnect'):
                    # inform other player about leaving and destroy the game
                    other = 'join' if is_host else 'host'
                    self.send(self._clients[game_id][other]['connection'], self._clients[game_id][other]['address'], SERVER_INFO['disconnect'])
                    del self._clients[game_id]
                else:
                    self.send(self._clients[game_id][client]['connection'], self._clients[game_id][client]['address'], message)

        except JSONDecodeError:
            raise ValueError('Invalid message format')
