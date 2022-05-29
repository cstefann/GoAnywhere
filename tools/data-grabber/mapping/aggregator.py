from formatting import formatter as f
from datetime import date, timedelta
import mapper as m

m.mapper()

prevDateFile = "mapper_outputs/" + str(date.today() - timedelta(days=1)) + ".txt"
currDateFile = "mapper_outputs/" + str(date.today()) + ".txt"
patternPath = "routes_patterns/"

def status_provider():
    with open(currDateFile) as curr, open(prevDateFile) as prev:
        currLines = curr.readlines()
        prevLines = prev.readlines()
        for currL, prevL in zip(currLines, prevLines):
            mapArray = f.cleanner(f.comparator(currL, prevL))
            dataFromFile = f.provide_last_line(mapArray[0].replace("'", ""))
            print(mapArray[1], dataFromFile)

status_provider()
