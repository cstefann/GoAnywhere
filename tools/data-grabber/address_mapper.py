# import pandas as pd
# import geopandas as gpd
# import geopy
# from geopy.geocoders import Nominatim
import geopy as gp
# from geopy.extra.rate_limiter import RateLimiter
# import matplotlib.pyplot as plt
# import plotly_express as px
# import tqdm
# from tqdm.notebook import tqdm_notebook

def coordinates_to_address(coordinates):
    locator = gp.Nominatim(user_agent="GoAnywhere_vehicle_data")
    location = locator.reverse(coordinates)
    location.raw
    return location.address

# def iterate_over_coordinates_from_file(filePath):
#     newFile = open("address_1946.txt", "x")
#     with open(filePath) as f:
#         lines = f.readlines()
#         for line in lines:
#             address = coordinates_to_address(line.strip()) + "\n"
#             print(address)
#             newFile.write(address)

# iterate_over_coordinates_from_file("../data/1946.txt")
