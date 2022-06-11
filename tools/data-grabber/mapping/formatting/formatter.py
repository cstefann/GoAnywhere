import os.path as os
import os as o
import hashlib

# Constant #
noise = ".DS_Store"
dataPath = "data/"
# Constants - End #

def elim_duplicates(inputFile, path):
    hashLines = set()
    if (os.exists(path + inputFile) == False):
        out = open(path + inputFile, "x")
    else:
        out = open(path + inputFile, "w")
    for line in open(dataPath + inputFile, "r"):
        hashValue = hashlib.md5(line.rstrip().encode('utf-8')).hexdigest()
        if hashValue not in hashLines:
            out.write(line)
            hashLines.add(hashValue)
    out.close()

# Function that gets rid of diacritics which helps in the match verification #
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

# Function that deletes a temp file generated in macOS #
def elim_noise(filePath):
    if (os.exists(filePath + noise)):
        os.remove(filePath + noise)
    return filePath

# Function the efficiently provides the last line of given file #
def provide_last_line(fileName):
    with open(fileName, "rb") as file:
        try:
            file.seek(-2, os.SEEK_END)
            while file.read(1) != b'\n':
                file.seek(-2, os.SEEK_CUR)
        except OSError:
            file.seek(0)
        last_line = file.readline().decode()
    return last_line

'''
Function that formats the output to obtain the relevant
data from the file names after mapping proc
'''
def cleanner(string):
    string[1] = string[1].replace(",", "")
    string[0] = string[0].replace("['", "").split("_", 2)[:2]
    return [str(string[1]), str((string[0]))]

# Function used in comparing the match ratios #
def comparator(string1, string2):
    string1 = string1.split()
    string2 = string2.split()
    if (float(string1[2].replace("]", "")) >= float(string2[2].replace("]", ""))):
        if (os.exists(string1[1].replace(",", "").replace("'", "")) == False):
            return string2
        else:
            return string1
    else:
        if (os.exists(string2[1].replace(",", "").replace("'", "")) == False):
            return string1
        else:
            return string2
