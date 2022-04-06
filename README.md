# GoAnywhere
Mobile application that gives you public transport suggestions so you can arrive as quick as possible at your desired destination.v

# Business logic

# Context:
GoAnywhere is an application that suggests the user real-time public transport suggestions from the current location to a given location.

# Detailed description:
- The user logs in, and enters a destination.
- The user will be routed to the nearest station. When he reaches it, a push notification will be sent and the location of the nearest station will be       swapped with the initial destination. 
- A list of routes suggestions will be provided so the user can select which route to take. 
- A map view will open, and below that, the user will see all the avalible routes with the remaning time of the bus to reach the station and the needed       amount of time for the bus to arrive at the desired destination.
- Each of the routes will have an unique color and markup on the map when selected. 
- After the user selects a route, a pop-up will show, with the map and the markup of the selected route. This view will contain:
	- Arrival time to the destination from the current location
	- Length ( number of stops) 
	- Real-time location of the bus ( the bus will "check-in" when it reaches a station, and this will appear in real time in the user interface, so the user     knows in how much time the bus will arrive at the nearest station from the user's current location)
- User can confirm in order to select a route, or go back to the list of routes. 
- If the user confirms the route, it will have a live view with a map, and the user location will be visible as a live markup.
- A push notification will be received when the user has 2 stations left until it reaches the destination.

# Improvements:
- Push notifications for the user ( when the user is in the bus) about upcoming tourist attractions with details, e.g: "In 50 meters, if you look to the     left/right side of the road, you will see Point of Interest X".
- Let the user know if a bus is full and suggest alternative routes with buses that have less people in them.
