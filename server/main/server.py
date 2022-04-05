import socket

from threading import Thread


class Server:
    ADDRESS = ('localhost', 5050)
    DISCONNECT = 'GWAndroidDisconnect'
    BUFFER = 16384  # 16kB

    @staticmethod
    def start():
        Thread(target=lambda: Server().listen(), daemon=True).start()

    def __init__(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind(Server.ADDRESS)

    def listen(self):
        self.socket.listen()
        while True:
            connection, address = self.socket.accept()
            client_thread = Thread(target=self._handle_client, args=[connection, address], daemon=True)
            client_thread.start()

    def _handle_client(self, connection, address):
        while True:
            message = connection.recv(Server.BUFFER).decode('utf-8')
            if message == Server.DISCONNECT:
                break

        connection.close()

