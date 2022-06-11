from datetime import datetime as dt
import requests as req
import os.path as os
import json as js
import time as t

# Constant #
URL = "https://gps.sctpiasi.ro/json"
disfunc = "1970-01-01 02:00:00"
filePath = "mapping/data/"
# Constants - End #

# Request to CTP for grabbing bus data and decode - json #
def data_downloader(URL):
    response = req.get(URL)
    return response.content.decode('ascii')

'''
Json parses that takes every json component and
creates a file for every bus/ tram
'''
def json_parser():
    jsonBlob = js.loads(data_downloader(URL))
    for i in jsonBlob:
        # check if the vehicle is moving by the timestamp
        if (i['vehicleDate'] != disfunc):
            # create/ append to specific file for every bus/ tram named after the vehicle id from json
            fileName = filePath + i['vehicleName'].replace(" ", "") + ".txt"
            if (os.exists(fileName) == False):
                f = open(fileName, "x")
            else:
                f = open(fileName, "a")
            coordinates = i['vehicleLat'] + "," + i['vehicleLong']
            f.write(coordinates + "\n")
            f.close()

def restart_uppon_timeout():
    try:
        # Loop that permits the grabbing script to continuosly run in background 
        while (True):
            print(dt.now().strftime("%H:%M:%S"))
            json_parser()
            t.sleep(60)
    except Exception:
        print("Restarting ...\n")
        restart_uppon_timeout()

restart_uppon_timeout()
