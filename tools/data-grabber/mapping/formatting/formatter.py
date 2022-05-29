import os as os
import os.path as path

noise = ".DS_Store"

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

def elim_noise(filePath):
    if (path.exists(filePath + noise)):
        os.remove(filePath + noise)
    return filePath

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

def cleanner(string):
    string[1] = string[1].replace(",", "")
    string[0] = string[0].replace("['", "").split("_", 2)[:2]
    return [str(string[1]), str((string[0]))]

def comparator(string1, string2):
    string1 = string1.split()
    string2 = string2.split()
    if (float(string1[2].replace("]", "")) >= float(string2[2].replace("]", ""))):
        return string1
    else:
        return string2
