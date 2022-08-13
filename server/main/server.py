import socket
from threading import Thread
from json import JSONDecodeError, loads

SERVER_INFO = {
    'host': socket.gethostbyname(socket.gethostname()),
    'port': 5050,
    'buffer': 1024,  # 1kB
    'format': 'utf-8',

    'create': '$_create',           # create new game
    'join': '$_join',               # connect to game
    'start': '$_start',             # players can communicate
    'handshake': '$_handshake',     # establish connection between players
    'disconnect': '$_disconnect',   # disconnect from game
    'invalid': '$_invalid',         # invalid game id
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
        print('[SEND]', address, message)
        connection.sendto(message, address)

    def receive(self, connection, address):
        message = connection.recv(SERVER_INFO['buffer']).decode(SERVER_INFO['format']).strip()
        print('[RECV]', address, message)
        return message

    def _handle_client(self, connection, address):
        try:
            while (message := self.receive(connection, address)) != SERVER_INFO['disconnect']:
                if message == SERVER_INFO['create']:
                    self._create_game(connection, address)
                elif message == SERVER_INFO['join']:
                    game_id = self.receive(connection, address).upper()
                    self._join_game(connection, address, game_id)
                elif message == SERVER_INFO['handshake']:
                    game_id = self.receive(connection, address).upper()
                    filename = self.receive(connection, address)
                    self._handshake(game_id, filename)
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

        if not match(SERVER_INFO['id_pattern'], game_id) or self._clients.get(game_id) is None:
            self.send(connection, address, SERVER_INFO['invalid'])
        else:
            # establish connection
            self._clients[game_id]['join']['connection'] = connection
            self._clients[game_id]['join']['address'] = address

            # send to host
            self.send(self._clients[game_id]['host']['connection'], self._clients[game_id]['host']['address'], SERVER_INFO['handshake'])
            
            # send to join
            self.send(self._clients[game_id]['join']['connection'], self._clients[game_id]['join']['address'], game_id)
            self.send(self._clients[game_id]['join']['connection'], self._clients[game_id]['join']['address'], SERVER_INFO['handshake'])

    def _handshake(self, game_id, filename):
        self.send(self._clients[game_id]['join']['connection'], self._clients['join']['address'], filename)

    def _communicate(self, message):
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
