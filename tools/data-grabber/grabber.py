from datetime import datetime as dt
import constants as const
import requests as req
import os.path as path
import os as os
import json as js
from mapping import mapper
import threading

# Request to CTP for grabbing bus data and decode - json #
def data_downloader(URL):
    response = req.get(URL)
    return response.content.decode('ascii')

def cleanup(filePath):
    print("[cleanup] Deleting files in" + str(filePath))
    dataFiles = os.listdir(filePath)
    for iter in dataFiles:
        os.remove(filePath + iter)

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
            if (path.exists(fileName) == False):
                f = open(fileName, "x")
            else:
                f = open(fileName, "a")
            output = i['vehicleLat'] + "," + i['vehicleLong'] + ";" + str(i["vehicleDate"]).split(" ")[1]
            f.write(output + "\n")
            f.close()

def map_thread():
    print("Paused data-grabbing, now mapping avalible data.")
    mapper.mapper()

# Function that restarts the grabber script in case of timeouts from CTP Open Data service #
def grabber(mappTime):
    currTime = dt.now().strftime("%H:%M:%S")
    splittedCurrTime = currTime.split(":")
    if (mappTime == "00:00:00"):
        mappTime = splittedCurrTime
        cleanup("mapping/data/")
        cleanup("mapping/filtered-data/")
    if (int(splittedCurrTime[0]) - int(mappTime[0]) == 4):
        mapperThread = threading.Thread(target=map_thread)
        mapperThread.start()
        mapperThread.join()
        print("[grabber-log] Terminated mapping thread\n")
        mappTime = "00:00:00"
    else:
        print(currTime)
        json_parser()
    return mappTime
