import json, socket
from threading import Thread

SERVER_INFO = {
    'host': socket.gethostbyname(socket.gethostname()),
    'port': 5050,
    'buffer': 1024,  # 1kB
    'format': 'utf-8',

    'create': '$_create',           # create new game
    'join': '$_join',               # connect to game
    'start': '$_start',             # players can communicate
    'ready': '$_ready',             # joining player is ready
    'disconnect': '$_disconnect',   # disconnect from game
    'invalid': '$_invalid',         # invalid game id
    'id_pattern': '^[A-Z0-9]{4}$',
}

_CLOSE_MESSAGES = (SERVER_INFO['disconnect'], '', None)


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

    def close(self, address):
        def disconnect(game_id, player):
            other = 'host' if player == 'join' else 'join'
            self._clients[game_id][player]['connection'].close()
            self._clients[game_id][player]['connection'] = None
            self._clients[game_id][player]['address'] = None

            # disconnect other player if they're still connected
            if self._clients[game_id][other]['connection']:
                self.send(self._clients[game_id][other]['connection'], self._clients[game_id][other]['address'], SERVER_INFO['disconnect'])

        for game_id in self._clients:
            if address == self._clients[game_id]['host']['address']:
                disconnect(game_id, 'host')
            elif address == self._clients[game_id]['join']['address']:
                disconnect(game_id, 'join')

            # destroy game if no player is connected
            if self._clients[game_id]['host']['connection'] is None and self._clients[game_id]['join']['connection'] is None:
                del self._clients[game_id]
                print(f'[GAME] Destroyed "{game_id}" instance')
                break

    def _handle_client(self, connection, address):
        while (message := self.receive(connection, address)) not in _CLOSE_MESSAGES:
            try:
                if message == SERVER_INFO['create']:
                    filename = self.receive(connection, address)
                    self._create_game(connection, address, filename)
                elif message == SERVER_INFO['join']:
                    game_id = self.receive(connection, address)
                    self._join_game(connection, address, game_id)
                elif message == SERVER_INFO['ready']:
                    game_id = self.receive(connection, address)
                    self._start_game(address, game_id)
                else:
                    self._communicate(json.loads(message))
            
            except json.JSONDecodeError:
                pass

        self.close(address)

    def _create_game(self, connection, address, filename):
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
            },
            'project': filename,
        }

    def _join_game(self, connection, address, game_id):
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

    def _start_game(self, address, game_id):
        other = 'host' if self._clients[game_id]['join']['address'] == address else 'join'
        self.send(self._clients[game_id][other]['connection'], self._clients[game_id][other]['address'], SERVER_INFO['ready'])

    def _communicate(self, message):
        game_id = message.get('game_id')
        is_host = message.get('is_host')
        other = 'join' if is_host else 'host'

        if self._clients.get(game_id):
            self.send(self._clients[game_id][other]['connection'], self._clients[game_id][other]['address'], json.dumps(message))
