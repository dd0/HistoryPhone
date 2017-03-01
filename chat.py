import requests
import json
import argparse
import os

# the Windows console crashes when writing Unicode characters, this
# module is a fix for that
if os.name == 'nt':
    import win_unicode_console


def get_reply(host, uuid, message, raw):
    params = {'message': message, 'uuid': uuid}
    r = requests.get(host, params=params)
    if r.status_code == 200:
        if raw:
            return r.text
        else:
            return json.loads(r.text)['message'].replace('\\n', '\n')
    else:
        return 'Error: %s' % r.status_code


def make_url(host):
    if not str.startswith(host, 'http://'):
        host = 'http://' + host
    if not str.endswith(host, '/'):
        host += '/'
    return host + 'api/response'


def main():
    if os.name == 'nt':
        win_unicode_console.enable()

    parser = argparse.ArgumentParser()
    parser.add_argument('host', help='address of the chat server (for example localhost:12345)')
    parser.add_argument('--uuid', required=True, nargs=1, help='UUID of the object to talk with')
    parser.add_argument('--raw', action='store_true', help='print raw JSON responses instead of replies')
    args = parser.parse_args()

    while True:
        s = input('> ').strip()
        print(get_reply(make_url(args.host), args.uuid, s, args.raw))

if __name__ == '__main__':
    main()
