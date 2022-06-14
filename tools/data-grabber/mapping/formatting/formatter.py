import os.path as os
import os as o
import hashlib

# Constant #
noise = ".DS_Store"
dataPath = "mapping/data/"
# Constants - End #

# Filter for data/ directory - used on mapping process to have more relevant result #
def elim_duplicates(inputFile, path):
    hashLines = set()
    if (os.exists(path + inputFile) == False):
        out = open(path + inputFile, "x")
    else:
        out = open(path + inputFile, "w")
    for line in open(dataPath + inputFile, "r"):
        hashValue = hashlib.md5(line.rstrip().encode('utf-8')).hexdigest()
        if hashValue not in hashLines:
            output = line.split(";")[0] + "\n"
            out.write(output)
            hashLines.add(hashValue)
    out.close()

# Function that deletes a temp file generated in macOS #
def elim_noise(filePath):
    if (os.exists(filePath + noise)):
        os.remove(filePath + noise)
    return filePath

# Function that formats an array as a json #
def to_json(array):
    jsonOutput = "{"
    for iter in array:
        separatorCheck = " - " in iter
        endCheck = "~" in iter
        if(endCheck == True):
            formatedJsonLine = "},"
            jsonOutput = jsonOutput[:-1] + formatedJsonLine
        else:
            if (separatorCheck == False):
                formatedJsonLine = "\"" + iter + "\"" + ":{"
                jsonOutput = jsonOutput + formatedJsonLine
            else:
                splitted = iter.split(" - ")
                formatedJsonLine = "\"" + splitted[0] + "\"" + ":" + "\"" + splitted[1] + "\"" + ","
                jsonOutput = jsonOutput + formatedJsonLine
    jsonOutput = jsonOutput[:-1] + "}"
    return jsonOutput
