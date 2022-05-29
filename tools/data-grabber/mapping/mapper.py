import os as os
import os.path as path
from datetime import date
from formatting import formatter as f

dataPath = "data/"
patternPath = "routes_patterns/"
outputPath= "mapper_outputs/"

def match_ratio(dataFile, patternFile):
    matchCounter = 0
    patternCounter = 0
    with open(dataFile) as data, open(patternFile) as pattern:
        dataLines = data.readlines()
        patternLines = pattern.readlines()
        for patternLine in patternLines:
            patternLine = f.elim_diacritics(patternLine.rstrip(patternLine[-1]))
            patternCounter = patternCounter + 1
            for dataLine in dataLines:
                dataLine = f.elim_diacritics(dataLine)
                if (dataLine.find(patternLine) > -1):
                    matchCounter = matchCounter + 1
                    break
    return [dataFile, matchCounter / patternCounter]

def mapper():
    dataFiles = os.listdir(f.elim_noise(dataPath))
    patternFiles = os.listdir(f.elim_noise(patternPath))
    currDate = str(date.today())
    if (path.exists(outputPath + currDate + ".txt") == False):
        outFile = open(outputPath + currDate + ".txt", "x")
    else:
        outFile = open(outputPath + currDate + ".txt", "w")
    for p in patternFiles:
        print("[log] Mapping " + p.split("_", 2)[0] + p.split("_", 2)[1])
        max = [p,"",0]
        for d in dataFiles:
            retVal = match_ratio(dataPath + d, patternPath + p)
            if (retVal[1] > max[2]):
                max[1] = retVal[0]
                max[2] = retVal[1]
        outFile.write(str(max) + "\n")
    outFile.close()
    print("[log] See the result in " + outputPath + "current_date")
