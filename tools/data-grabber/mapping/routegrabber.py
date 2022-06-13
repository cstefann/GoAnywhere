import constants as const
import requests as req
import json as js

# Grab data for all buses and trams #
def data_downloader(id):
    URL = const.apiURL + "/apiPublic/route/byId/" + str(id)
    response = req.get(URL)
    return response.content

# Appends to dataArr array all the stops without duplicates for one vehicle #
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

# Appends to dataArr array all the coordinates that are checked in by a vehicle - used on mapping for relevant results #
def shapes(jsonBlob, vehicleID, dataArr):
    vehArr = [(str(vehicleID).split("_")[0], str(vehicleID).split("_")[1])]
    for i in jsonBlob["data"]["routeWayCoordinates"]:
        vehArr.append((float(i["lat"]), float(i["lng"])))
    dataArr.append(vehArr)

# Creates a data array with the selected type - shapes or stops #
def iterator(wantedFile, id_mapping):
    dataArr = []
    print("[route-grabber] Getting " + wantedFile + " for all vehicles")
    with open (id_mapping) as mapped:
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
