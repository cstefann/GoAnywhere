import os.path as os
import os as o
import hashlib
from posixpath import split

# Constant #
noise = ".DS_Store"
dataPath = "mapping/data/"
# Constants - End #

# Eliminate diacritics from output to solve encoding issues #
def elim_diacritics(text):
     text = text.replace('â', 'a')
     text = text.replace('ă', 'a')
     text = text.replace('Ă', 'A')
     text = text.replace('Â', 'A')
     text = text.replace('î', 'i')
     text = text.replace('î', 'I')
     text = text.replace('ț', 't')
     text = text.replace('Ț', 'T')
     text = text.replace('ș', 's')
     text = text.replace('Ș', 'S')
     return text

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
    jsonOutput = "" + array[0]
    array.pop(0)
    for iter in array:
        if (type(iter) == tuple):
            splitted1 = iter[0].split(" - ")
            splitted2 = iter[1].split(" - ")
            formatedJsonLine = "{" + "\"" + str(splitted1[0]) + "\"" + " : " + "\"" + str(splitted1[1]) + "\"" + ", "
            formatedJsonLine = formatedJsonLine +  "\"" + str(splitted2[0]) + "\"" + " : " + "\"" + str(splitted2[1]) + "\"" + "},"
            jsonOutput = jsonOutput + formatedJsonLine
        else:
            separatorCheck = " - " in iter
            endCheck = "~" in iter
            if(endCheck == True):
                formatedJsonLine = "],"
                jsonOutput = jsonOutput[:-1] + formatedJsonLine
            else:
                if (separatorCheck == False):
                    formatedJsonLine = "\"" + iter + "\"" + " : ["
                    jsonOutput = jsonOutput + formatedJsonLine
                else:
                    splitted = iter.split(" - ")
                    formatedJsonLine = "\"" + splitted[0] + "\"" + ":" + "\"" + splitted[1] + "\"" + ","
                    jsonOutput = jsonOutput + formatedJsonLine
    if (jsonOutput[0] == "{"):
        jsonOutput = jsonOutput[:-1] + "}"
    else:
        jsonOutput = jsonOutput[:-1] + "]"
    return jsonOutput
