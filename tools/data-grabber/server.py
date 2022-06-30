from http.server import BaseHTTPRequestHandler, HTTPServer
import threading
import grabber as gb
import provide_route as feeder
import time as t

hostName = "localhost"
serverPort = 3000

# Function that restarts the grabber script in case of timeouts from CTP Open Data service #
def grab_data(currTime, run_event):
    try:
        # Loop that permits the grabbing script to continuosly run in background     
        while run_event.is_set():
            currTime = gb.grabber(currTime)
            t.sleep(60)
    except Exception:
        print("Restarting because of timeout ...\n")
        grab_data(currTime, run_event)

def server_response(self, response, content):
    self.send_response(200)
    self.send_header('Content-Type', content)
    self.end_headers()
    self.wfile.write(bytes(response, 'UTF-8'))
 
def getroutes_thread_func(self, path):
    params = path[1].split("&")
    currentCoord = (float(params[0].split("=")[1]), float(params[1].split("=")[1]))
    destCoord = (float(params[2].split("=")[1]), float(params[3].split("=")[1]))                                
    response = feeder.get_routes(currentCoord, destCoord)[0]
    server_response(self, response, 'application/json')

def providecoords_thread_func(self, path):
    destination = path[1].split("=")[1]
    response = str(feeder.provide_coordinates(destination))
    server_response(self, response, 'application/json')

def last_status_thread_function(self, path):
    vehicle = path[1].split("=")[1]
    response = str(feeder.provide_last_status(vehicle)[0]).split(",")
    print(response)
    output = "{" + "\"latitude\" : " + response[0] + "," + "\"longitude\" : " + response[1] + "}"
    server_response(self, output, 'application/json')

def inforoute_thread_function(self, path):
    params = path[1].split("&")
    currentCoord = (float(params[0].split("=")[1]), float(params[1].split("=")[1]))
    destCoord = (float(params[2].split("=")[1]), float(params[3].split("=")[1]))  
    routeName = str(params[4].split("=")[1])
    routeNumber = int(params[5].split("=")[1])
    response = feeder.provide_info_route(routeName, routeNumber, currentCoord, destCoord)
    server_response(self, response, 'application/json')

class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        path = self.path.split("?")
        route = path[0]
        if (route == "/getroutes"):
            getRoutesThread = threading.Thread(target = getroutes_thread_func, args=(self,path,))
            getRoutesThread.start()
            getRoutesThread.join()
            print("[server-log] Terminated get_routes thread\n")
        elif (route == "/providecoords"):
            provideCoordsThread = threading.Thread(target = providecoords_thread_func, args=(self,path,))
            provideCoordsThread.start()
            provideCoordsThread.join()
            print("[server-log] Terminated provide_coords thread\n")
        elif (route == "/inforoute"):
            infoRouteThread = threading.Thread(target = inforoute_thread_function, args=(self,path,))
            infoRouteThread.start()
            infoRouteThread.join()
            print("[server-log] Terminated info_route thread\n")
        elif (route == "/status"):
            lastStatusThread = threading.Thread(target = last_status_thread_function, args=(self,path,))
            lastStatusThread.start()
            lastStatusThread.join()
            print("[server-log] Terminated last_status thread\n")

if __name__ == "__main__":        
    currTime = "00:00:00"
    webServer = HTTPServer((hostName, serverPort), MyServer)
    print("Server started http://%s:%s" % (hostName, serverPort))
    run_event = threading.Event()
    run_event.set()
    grabberThread = threading.Thread(target = grab_data, args=(currTime,run_event,))
    try:
        grabberThread.start()
        webServer.serve_forever()
    except KeyboardInterrupt:
        print("\nReceived KB Interrupt ... waiting for all the procs to close\n")
        run_event.clear()
        grabberThread.join()
        print("Data-grab thread closed.")
        webServer.server_close()
        print("Server stopped.")
