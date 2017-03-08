import os
from termcolor import cprint
import atexit
from http.server import HTTPServer, BaseHTTPRequestHandler
import urllib.parse as urlparse
import random
import time
import threading
import json
import requests

# this is a very fragile hack, but it should work well enough to use
# it once or twice during the demo
questions = {}
resp_queue = {}


class handler(BaseHTTPRequestHandler):

    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-Type', 'text/json')
        self.end_headers()

        msg = urlparse.parse_qs(urlparse.urlparse(self.path).query).get('msg')
        msg = msg[0]  # .get() returns an array

        global resp_queue
        global questions
        key = ''.join([chr(random.randint(0, 25)+ord('a')) for i in range(10)])
        questions[key] = msg
        resp_queue[key] = None

        while resp_queue[key] is None:
            time.sleep(0.25)

        res = json.dumps({'response': resp_queue[key]})

        self.wfile.write(bytes(res, 'utf-8'))
        return

    # don't log requests
    def log_message(self, format, *args):
        return


def main():
    port_num = 31532
    addr = ('localhost', port_num)

    cprint('Starting server on %s:%d... ' % addr, 'cyan', end='')
    server = HTTPServer(addr, handler)
    threading.Thread(target=server.serve_forever, daemon=True).start()
    cprint('Done.', 'green')

    cprint('Sending sethandler query... ', 'cyan', end='')

    requests.get('http://localhost:12345/api/sethandler',
                 params={'type': 'manual', 'port': port_num})

    cprint('Done.', 'green')
    cprint('Response server is now running.', 'cyan')
    cprint('Do not press Ctrl-C more than once when exiting!', 'red')
    cprint('It can get the server stuck in manual mode.', 'red')
    print()

    while True:
        for k in resp_queue.keys():
            if resp_queue[k] is None:
                cprint(questions[k], 'yellow')
                resp_queue[k] = input('> ').strip()

        time.sleep(0.1)


def cleanup():
    cprint('Exiting, switching back to LUIS...', 'cyan', end='')
    requests.get('http://localhost:12345/api/sethandler',
                 params={'type': 'luis'})
    cprint('Done.', 'green')

if __name__ == '__main__':
    if os.name == 'nt':
        import win_unicode_console
        win_unicode_console.enable()

        import colorama
        colorama.init()

    atexit.register(cleanup)
    main()
