from mapping import routegrabber as rg
from haversine import haversine, Unit
import constants as const
import requests as req
import json as js
import math as m

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

# Provide nearest station from the users's current location #
def provide_nearest_station(lat, long):
    stopsArr = get_all_stops()
    minDistance = 10000.0
    nearestStation = ("", 0.0, 0.0, "")
    for iter in stopsArr:
        iterCoord = (iter[1], iter[2])
        currentDistance = distance((lat, long), iterCoord)
        if (currentDistance < minDistance):
            minDistance = currentDistance
            nearestStation = iter
    return nearestStation

# Coordinates for destination - Autocomplete feature for addreses, e.g: Iulius Mall as input -> full address with coordinates #
def provide_coordinates(dest):
    destURL = const.addrURL + dest + const.addrFormatter
    destContent = js.loads(data_downloader(destURL))
    destCoord = (destContent[1]["lat"], destContent[1]["lon"])
    return destCoord

# Function that provides all avalible routes from nearest station to destination #
def get_routes(current, dest):
    routeURL = const.apiURL + "/routing/routes/" + str(current[0]) + "/" + str(current[1]) + "/" + str(dest[0]) + "/" + str(dest[1])
    avalibleRoutes = []
    routes = js.loads(data_downloader(routeURL))
    for i in routes["data"]:
            avalibleRoutes.append(str(len(avalibleRoutes)) + " - " + str(i["routes"][0]['routeName']))
    return [avalibleRoutes, routes["data"]]

# Provide info for selected route - all the stops, number of stops to the dest, aproximate arrival time to dest #
def provide_info_route(data, routeNumber):
    for i in data[routeNumber]["routes"][0]["routeWaypoints"]:
        print(i["name"])
    print("Numar statii de la locatia curenta la destinatie: " + str(data[routeNumber]["nrStatii"]))
    print("Timp de deplasare locatia curenta la destinatie: " + str(m.ceil(data[routeNumber]["timpDrive"])) + " min")
        
# For debugging purpose #
# destCoord = provide_coordinates("Minerva")
# currentCoord = (47.1665800, 27.5561825) # spoofed current location
# print(get_routes(currentCoord, destCoord)[0])
# provide_info_route(get_routes(currentCoord, destCoord)[1], 0)
get_all_stops()
