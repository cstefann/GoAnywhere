from dis import dis
from mapping import routegrabber as rg
from haversine import haversine, Unit
import constants as const
import requests as req
import json as js
import math as m
import os
from mapping.formatting import formatter as f
import math as m

mappedPath = "mapping/mapper_outputs/"
dataPath = "mapping/data/"
headlines = "mapping/headlines.txt"

# GET request to Wink public API to gether coordinates for destination stop #
def data_downloader(URL):
    response = req.get(URL)
    return response.content.decode('UTF-8')

# Calculate distance between two coordinates - used on nearest_station #
def distance(patternCoord, dataCoord):
    return haversine(patternCoord, dataCoord, unit=Unit.METERS)

# Obtain an array with all the stops - used on nearest_station function #
def get_all_stops():
    dataArr = rg.iterator("stops", "mapping/id_mapping.txt")
    return dataArr

# Provide last location of a bus/ tram using data files generated from CTP Open Data after mapping #       
def provide_last_location_bus_tram(filename):
    with open(filename, 'rb') as data:
        try:
            data.seek(-2, os.SEEK_END)
            while data.read(1) != b'\n':
                data.seek(-2, os.SEEK_CUR)
        except OSError:
            data.seek(0)
        last_line = data.readline().decode()
    data.close()
    return last_line

# --- Functions that will be called in server endpoints --- #

# Coordinates for destination - Autocomplete feature for addreses, e.g: Iulius Mall as input -> full address with coordinates #
def provide_coordinates(dest):
    print("Received dest: " + dest + "\n")
    destURL = const.addrURL + dest + const.addrFormatter
    print(destURL)
    destContent = js.loads(data_downloader(destURL))
    return "{" + "\"latitude\" : " + str(destContent[0]["lat"]) + "," + "\"longitude\" : " + str(destContent[0]["lon"]) + "}"

# Provide nearest station from the users's current location #
def provide_nearest_station(coords, vehicle):
    stopsArr = get_all_stops()
    minDistance = 10000.0
    currentDistance = 10000.0
    nearestStation = ("", 0.0, 0.0, "")
    for iter in stopsArr:
        isVehiclePresent = vehicle in iter[3]
        if (isVehiclePresent == True):
            iterCoord = (iter[1], iter[2])
            currentDistance = distance((coords[0], coords[1]), iterCoord)
            if (currentDistance < minDistance):
                minDistance = currentDistance
                nearestStation = iter
    return nearestStation

# Function that provides all avalible routes from nearest station to destination #
def get_routes(current, dest):
    avalibleRoutes = "["
    id = 0
    routeURL = const.apiURL + "/routing/routes/" + str(current[0]) + "/" + str(current[1]) + "/" + str(dest[0]) + "/" + str(dest[1])
    alreadyAppended = ""
    routes = js.loads(data_downloader(routeURL))
    if (dest[0] == 0.0 and dest[1] == 0.0):
        with open (headlines) as h:
            lines = h.readlines()
            for iter in lines:
                avalibleRoutes = avalibleRoutes + ("{" + "\"id\" : " + "\"" + str(id) + "\"" + "," + "\"route\" : " + "\"" + iter.replace("\n", "") + "\"" + "}" + ",")
                id = id + 1
            avalibleRoutes = avalibleRoutes[:-1] + "]"
        h.close()
        return [avalibleRoutes, routes["data"]]
    else:
        avalibleRoutes = ["["]
        for i in routes["data"]:
            check = str(i["routes"][0]['routeName']) in alreadyAppended
            if (check == False):
                avalibleRoutes.append(("id - " + str(id), "route - " + str(i["routes"][0]['routeName']).replace("5 TRAMVAI", "Tramvai 5")))
                alreadyAppended = alreadyAppended + " " + str(i["routes"][0]['routeName'])
            id = id + 1
        return [f.to_json(avalibleRoutes), routes["data"]]

def provide_last_status(routeName):
    mappedFileName = "mapped_vehicles.txt"
    dataFile = ""
    with open(mappedPath + mappedFileName) as map:
        lines = map.readlines()
        for iter in lines:
            iterSplitted = iter.replace("[", "").replace("]", "").replace("'", "").split(",")
            if(iterSplitted[0] == routeName.replace("_", " ")):
                dataFile = str(iterSplitted[1]).replace(" ", "")
                break
    map.close()
    print(dataFile)
    return provide_last_location_bus_tram(dataPath + dataFile).split(";")

# Provide info for selected route - all the stops, number of stops to the dest, aproximate arrival time to dest #
def provide_info_route(routeName, routeNumber, currentCoord, destCoord):
    output = ["{"]
    nearestCurrentStation = provide_nearest_station(currentCoord, routeName)
    data = get_routes((currentCoord[0], currentCoord[1]), (destCoord[0], destCoord[1]))[1]
    output.append("nearestStation - " + str(nearestCurrentStation[0]))
    output.append("vehicul - " + routeName)
    output.append("ruta")
    counter = 1

    for i in data[routeNumber]["routes"][0]["routeWaypoints"]:
        if (i["name"] == data[routeNumber]["routes"][0]["statieSosireNume"]):
            output.append(("id - " + str(counter), "statie - " + f.elim_diacritics(i["name"]) + " (sosire)"))
        elif (i["name"] == data[routeNumber]["routes"][0]["statiePlecareNume"]):
            output.append(("id - " + str(counter), "statie - " + f.elim_diacritics(i["name"]) + " (plecare)"))
        else: 
            output.append(("id - " + str(counter), "statie - " + f.elim_diacritics(i["name"])))
        counter = counter + 1

    output.append("~")
    vehInfo = provide_last_status(routeName)
    vehCoords = vehInfo[0].split(",")
    output.append("locatie - " + str(vehInfo[0]))
    output.append("ora - " + str(vehInfo[1]).replace("\n", ""))
    output.append("distanta - " + str(m.ceil(distance((nearestCurrentStation[1], nearestCurrentStation[2]), (float(vehCoords[0]), float(vehCoords[1]))))) + " metri")
    output.append("nrStatii - " + str(data[routeNumber]["nrStatii"]))
    output.append("arrivalTime - " + str(m.ceil(data[routeNumber]["timpDrive"])) + " min")
    return f.to_json(output)
