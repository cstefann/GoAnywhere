from haversine import haversine, Unit
from mapping.formatting import formatter as f
from mapping import routegrabber as rg
import os.path as path
import os as os

# Filepaths #
dataPath = "mapping/data/"
outputPath= "mapping/mapper_outputs/"
filteredPath = "mapping/filtered-data/"

# Calculate distance between two coordinates, used on match_ratio #
def distance(patternCoord, dataCoord):
    return haversine(patternCoord, dataCoord, unit=Unit.METERS)

'''
Function the takes one data file with one pattern and compares them.
Searches every line from pattern into data file and when
current line from pattern is found as an substring in one of datafile
line, a match is counted and goes to the next line.
'''
def match_ratio(dataFile, patternArr):
    matchCounter = 0
    with open(dataFile) as data:
        dataLines = data.readlines()
        for iter in patternArr:
            for dataLine in dataLines:
                splittedDataLine = dataLine.split(",")
                dataCoord = (float(splittedDataLine[0]), float(splittedDataLine[1]))
                if (distance(iter, dataCoord) < 11.0):
                    matchCounter = matchCounter + 1
                    break
    if (matchCounter == 0):
        return [str(dataFile).split("/")[2], 0]
    else:
        return [str(dataFile).split("/")[2], matchCounter / len(patternArr)]

'''
Function that takes every pattern and verifies it with all the datafiles
Applies match ratio on all of the pairs and calculates the max ratio from
all the matches calculated for one pattern with all data files.
After that, it generates a file where the mappings will be avalible, e. g:
['tram_3_pattern.txt', 'data/101.txt', 1.0]
'''
def mapper():
    dataFiles = os.listdir(f.elim_noise(dataPath))
    fileName = "mapped_vehicles.txt"
    dataArr = rg.iterator("shapes", "mapping/id_mapping.txt")
    if (path.exists(outputPath + fileName) == False):
        outFile = open(outputPath + fileName, "x")
    else:
        outFile = open(outputPath + fileName, "w")
    print("[mapper] Started mapping!")
    for iter in dataArr:
        max = [str(iter[0][0]) + " " + str(iter[0][1]),"",0]
        iter.pop(0)
        print("[mapper] Mapping " + max[0])
        for d in dataFiles:
            f.elim_duplicates(d, filteredPath)
            retVal = match_ratio(filteredPath + d, iter)
            if (retVal[1] > max[2]):
                max[1] = retVal[0]
                max[2] = retVal[1]
        outFile.write(str(max) + "\n")
    outFile.close()
    print("[mapper] Done mapping!")
