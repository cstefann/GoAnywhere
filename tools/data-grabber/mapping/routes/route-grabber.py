import requests as req
import os.path as path
import json as js

api = "https://m-go-iasi.wink.ro/apiPublic/"

def data_downloader(id):
    URL = api + "route/byId/" + str(id)
    response = req.get(URL)
    return response.content

def stations(jsonBlob, name):
    if (path.exists(name + ".txt") == False):
        outFile = open(name + ".txt", "x")
    else:
        outFile = open(name + ".txt", "w")
    for i in jsonBlob["data"]["routeWaypoints"]:
        if (i["name"] != "" and i["name"] != None):
            # print(i["name"])
            # print(i["lat"])
            # print(i["lng"])
            outStr = i["name"] + "," + i["lat"] + "," + i["lng"] + "\n"
            outFile.write(outStr)
    outFile.close()

def shape(jsonBlob, name):
    if (path.exists(name + ".txt") == False):
        outFile = open(name + ".txt", "x")
    else:
        outFile = open(name + ".txt", "w")
    for i in jsonBlob["data"]["routeWayCoordinates"]:
        outStr = i["lat"] + "," + i["lng"] + "\n"
        outFile.write(outStr)
    outFile.close()

def iterator():
    with open ("id_mapping.txt") as mapped:
        mappedLine = mapped.readlines()
        for iter in mappedLine:
            line = iter.split(",")
            line[2] = line[2].replace("\n", "")
            fileName = line[1] + "_" + line[2]
            print("[log] Collecting data for " + fileName)
            jsonBlob = js.loads(data_downloader(line[0]))
            stations(jsonBlob, "stops/" + fileName)
            shape(jsonBlob, "shapes/" + fileName)
    mapped.close()

iterator()