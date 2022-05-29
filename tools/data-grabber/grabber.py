from datetime import datetime as dt
import address_mapper as mapper
import requests as req
import os.path as os
import json as js
import time as t

# Constant #
URL = "https://gps.sctpiasi.ro/json"
disfunc = "1970-01-01 02:00:00"
filePath = "mapping/data/"
# Constants - End #

def data_downloader(URL):
    response = req.get(URL)
    return response.content.decode('ascii')

def content_to_file(lat, long):
    # return lat + ", " + long + ", " + time + "\n"
    return mapper.coordinates_to_address(lat + ", " + long)

def json_parser():
    jsonBlob = js.loads(data_downloader(URL))
    for i in jsonBlob:
        if (i['vehicleDate'] != disfunc):
            if (os.exists(filePath + i['vehicleName'] + ".txt") == False):
                f = open(filePath + i['vehicleName'] + ".txt", "x")
            else:
                f = open(filePath + i['vehicleName'] + ".txt", "a")
            addr = content_to_file(i['vehicleLat'], i['vehicleLong'])
            f.write(addr + "; " + i['vehicleDate'] + "\n")
            f.close()

while (True):
    print(dt.now().strftime("%H:%M:%S"))
    json_parser()
    t.sleep(120)
