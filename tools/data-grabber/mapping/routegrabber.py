import requests as req
import json as js

api = "https://m-go-iasi.wink.ro/apiPublic/"

def data_downloader(id):
    URL = api + "route/byId/" + str(id)
    response = req.get(URL)
    return response.content

def stops(jsonBlob, vehicleID, dataArr):
    for i in jsonBlob["data"]["routeWaypoints"]:
        if (i["name"] != "" and i["name"] != None):
            outLine = (i["name"], float(i["lat"]), float(i["lng"]), vehicleID)
            appended = False
            for iter in range(0, len(dataArr)):
                temp = list(dataArr[iter])
                if (temp[1] == outLine[1] and temp[2] == outLine[2]):
                    appendedAlready = vehicleID in str(temp[3]).replace(" ", "").split(",")
                    if (appendedAlready == False):
                        temp[3] = temp[3] + ", " + vehicleID
                        appended = True
                        dataArr[iter] = tuple(temp)
            if (appended == False):
                dataArr.append(outLine)

def shapes(jsonBlob, vehicleID, dataArr):
    vehArr = [(str(vehicleID).split("_")[0], str(vehicleID).split("_")[1])]
    for i in jsonBlob["data"]["routeWayCoordinates"]:
        vehArr.append((float(i["lat"]), float(i["lng"])))
    dataArr.append(vehArr)

def iterator(wantedFile):
    dataArr = []
    print("[route-grabber] Getting " + wantedFile + " for all vehicles")
    with open ("id_mapping.txt") as mapped:
        mappedLine = mapped.readlines()
        for iter in mappedLine:
            line = iter.split(",")
            line[2] = line[2].replace("\n", "")
            vehicleID = line[1] + "_" + line[2]
            print("[log] Collecting data for " + vehicleID)
            jsonBlob = js.loads(data_downloader(line[0]))
            if (wantedFile == "stops"):
                stops(jsonBlob, vehicleID, dataArr)
            elif (wantedFile == "shapes"):
                shapes(jsonBlob, vehicleID, dataArr)
    print("[route-grabber] Succes!")
    mapped.close()
    return dataArr
