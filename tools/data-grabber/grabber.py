from datetime import datetime as dt
import constants as const
import requests as req
import os.path as os
import json as js

# Request to CTP for grabbing bus data and decode - json #
def data_downloader(URL):
    response = req.get(URL)
    return response.content.decode('ascii')

'''
Json parses that takes every json component and
creates a file for every bus/ tram
'''
def json_parser():
    jsonBlob = js.loads(data_downloader(const.ctpURL))
    for i in jsonBlob:
        # check if the vehicle is moving by the timestamp
        if (i['vehicleDate'] != const.disfunc):
            # create/ append to specific file for every bus/ tram named after the vehicle id from json
            fileName = const.filePath + "." + i['vehicleName'].replace(" ", "") + ".txt"
            if (os.exists(fileName) == False):
                f = open(fileName, "x")
            else:
                f = open(fileName, "a")
            output = i['vehicleLat'] + "," + i['vehicleLong'] + ";" + str(i["vehicleDate"]).split(" ")[1]
            f.write(output + "\n")
            f.close()

# Function that restarts the grabber script in case of timeouts from CTP Open Data service #
def grabber():
    print(dt.now().strftime("%H:%M:%S"))
    json_parser()
