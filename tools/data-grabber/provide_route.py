from mapping import routegrabber as rg
from haversine import haversine, Unit
import constants as const
import requests as req
import json as js
import math as m
import os
from datetime import date
from mapping.formatting import formatter as f

mappedPath = "mapping/mapper_outputs/"
dataPath = "mapping/data/"

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
    destURL = const.addrURL + dest + const.addrFormatter
    destContent = js.loads(data_downloader(destURL))
    destCoord = (destContent[0]["lat"], destContent[0]["lon"])
    return destCoord

# Provide nearest station from the users's current location #
def provide_nearest_station(currLocation, destLocation, vehicle):
    stopsArr = get_all_stops()
    minDistanceCurr = 10000.0
    minDistanceDest = 10000.0
    nearestStationCurr = ("", 0.0, 0.0, "")
    nearestStationDest = ("", 0.0, 0.0, "")
    for iter in stopsArr:
        isVehiclePresent = vehicle in iter[3]
        if (isVehiclePresent == True):
            iterCoord = (iter[1], iter[2])
            currentDistanceCurr = distance((currLocation[0], currLocation[1]), iterCoord)
            currentDistanceDest = distance((destLocation[0], destLocation[1]), iterCoord)
            if (currentDistanceCurr < minDistanceCurr):
                minDistanceCurr = currentDistanceCurr
                nearestStationCurr = iter
            if (currentDistanceDest < minDistanceDest):
                minDistanceDest = currentDistanceCurr
                nearestStationDest = iter
    return [nearestStationCurr, nearestStationDest]

# Function that provides all avalible routes from nearest station to destination #
def get_routes(current, dest):
    routeURL = const.apiURL + "/routing/routes/" + str(current[0]) + "/" + str(current[1]) + "/" + str(dest[0]) + "/" + str(dest[1])
    avalibleRoutes = []
    routes = js.loads(data_downloader(routeURL))
    for i in routes["data"]:
            avalibleRoutes.append(str(len(avalibleRoutes)) + " - " + str(i["routes"][0]['routeName']).replace("5 TRAMVAI", "Tramvai 5"))
    return [f.to_json(avalibleRoutes), routes["data"]]

# Provide info for selected route - all the stops, number of stops to the dest, aproximate arrival time to dest #
def provide_info_route(routeName, routeNumber, currentCoord, destCoord):
    output = []
    nearestStations = provide_nearest_station(currentCoord, destCoord, routeName)
    nearestCurrentStation = nearestStations[0]
    nearestDestStation = nearestStations[1]
    data = get_routes((nearestCurrentStation[1], nearestCurrentStation[2]), (nearestDestStation[1], nearestDestStation[2]))[1]
    output.append("nearestStation - " + str(nearestCurrentStation))
    output.append("vehicul - " + routeName)
    output.append("ruta")
    counter = 1
    for i in data[routeNumber]["routes"][0]["routeWaypoints"]:
        if (i["name"] == data[routeNumber]["routes"][0]["statiePlecareNume"]):
            output.append("statie " + str(counter) + " (plecare) - " + f.elim_diacritics(i["name"]))
        elif (i["name"] == data[routeNumber]["routes"][0]["statieSosireNume"]):
            output.append("statie " + str(counter) + " (sosire) - " + f.elim_diacritics(i["name"]))
        else: output.append("statie " + str(counter) + " - " + f.elim_diacritics(i["name"]))
        counter = counter + 1
    output.append("~")
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
    print(dataPath + dataFile)
    vehInfo = provide_last_location_bus_tram(dataPath + dataFile).split(";")
    vehCoords = vehInfo[0].split(",")
    output.append("locatie - " + str(vehInfo[0]))
    output.append("ora - " + str(vehInfo[1]).replace("\n", ""))
    output.append("distanta - " + str(distance((nearestCurrentStation[1], nearestCurrentStation[2]), (float(vehCoords[0]), float(vehCoords[1])))))
    output.append("nrStatii - " + str(data[routeNumber]["nrStatii"]))
    output.append("arrivalTime - " + str(m.ceil(data[routeNumber]["timpDrive"])) + " min")
    return f.to_json(output)
