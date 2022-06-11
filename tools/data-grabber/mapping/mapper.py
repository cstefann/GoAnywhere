from haversine import haversine, Unit
from formatting import formatter as f
from datetime import date
import os.path as path
import os as os

# Constant #
dataPath = "data/"
patternPath = "routes/shapes/"
outputPath= "mapper_outputs/"
filteredPath = "filtered-data/"
# Constants - End #

def convertor(str):
    str = str.split(",")
    return (float(str[0]), float(str[1]))

def distance(patternCoord, dataCoord):
    return haversine(patternCoord, dataCoord, unit=Unit.METERS)

'''
Function the takes one data file with one pattern and compares them.
Searches every line from pattern into data file and when
current line from pattern is found as an substring in one of datafile
line, a match is counted and goes to the next line.
'''
def match_ratio(dataFile, patternFile):
    matchCounter = 0
    patternCounter = 0
    with open(dataFile) as data, open(patternFile) as pattern:
        dataLines = data.readlines()
        patternLines = pattern.readlines()
        for patternLine in patternLines:
            patternCounter = patternCounter + 1
            patternCoord = convertor(patternLine)
            # patternCoord = convertor(patternLine.split(",")[1] + "," + patternLine.split(",")[2])
            for dataLine in dataLines:
                dataCoord = convertor(dataLine)
                if (distance(patternCoord, dataCoord) < 11.0):
                    matchCounter = matchCounter + 1
                    break
    return [str(dataFile).split("/")[1], matchCounter / patternCounter]

'''
Function that takes every pattern and verifies it with all the datafiles
Applies match ratio on all of the pairs and calculates the max ratio from
all the matches calculated for one pattern with all data files.
After that, it generates a file where the mappings will be avalible, e. g:
['tram_3_pattern.txt', 'data/101.txt', 1.0]
'''
def mapper():
    dataFiles = os.listdir(f.elim_noise(dataPath))
    patternFiles = os.listdir(f.elim_noise(patternPath))
    currDate = str(date.today())
    if (path.exists(outputPath + currDate + ".txt") == False):
        outFile = open(outputPath + currDate + ".txt", "x")
    else:
        outFile = open(outputPath + currDate + ".txt", "w")
    for p in patternFiles:
        print("[debug] Mapping " + p.split("_", 2)[0] + p.split("_", 2)[1])
        max = [p,"",0]
        for d in dataFiles:
            f.elim_duplicates(d, filteredPath)
            retVal = match_ratio(filteredPath + d, patternPath + p)
            if (retVal[1] > max[2]):
                max[1] = retVal[0]
                max[2] = retVal[1]
        outFile.write(str(max) + "\n")
    outFile.close()
    print("[debug] Done mapping!")

mapper()