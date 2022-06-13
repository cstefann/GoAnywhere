from http.server import BaseHTTPRequestHandler, HTTPServer
import threading
import grabber as gb
import time as t

hostName = "localhost"
serverPort = 3000

# Function that restarts the grabber script in case of timeouts from CTP Open Data service #
def grab_data(run_event):
    while run_event.is_set():
        gb.grabber()
        t.sleep(60)

class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        

if __name__ == "__main__":        
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))
    run_event = threading.Event()
    run_event.set()
    t1 = threading.Thread(target = grab_data, args=(run_event,))
    try:
        t1.start()
        webServer.serve_forever()
    except KeyboardInterrupt:
        print("\nReceived KB Interrupt ... waiting for all the procs to close\n")
        run_event.clear()
        t1.join()
        print("Data-grab thread closed.")
        webServer.server_close()
        print("Server stopped.")
