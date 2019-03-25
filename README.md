# Meteorology AndroidApp

# 1-DESCRIPTION

In this application it is possible to visualize the updated meteorological forecast to the minute (in case of continuous connection to the internet) for the biggest cities of Portugal, including islands.
On the screen of each city you can see a small icon that represents the meteorological state as well as the forecasts of temperature, wind and rain. You can see the forecast for the current day as well as for the next two days by sliding horizontally between the tabs. For each city it is still possible to see the average temperatures and rain for the current month. This feature is only allowed in case the internet is connected.
At the bottom of each screen of each city it is still possible to see when it was the last update made to the information. If we do not have internet connection or the data is less than 1 minute in the database, then only the local data is used. Otherwise a refresh is made to data coming from external APIs.
For navigation between cities it is possible to click on the location icon at the top right of the screen of a city and then, on the map, click on the desired city's label (for viewing purposes, the islands are not visible on the initial map but simply slide to the left or zoom out to find them). You can also search for a city by clicking on the magnifying glass icon (also at the top right of each city) and typing the desired city name.
Note: All required and optional features have been implemented.

# 2-ARCHITECTURE

The architecture followed by the application is the architecture described in the "Guide to app architecture". Only the City class is mapped into the database having all the necessary details for a good operation of the app in local terms.
In terms of DAO, there is an insert (save) method and four methods that make queries to the database (getDistinctCities, getAllCities, load (city_name, day) and hasCity (city_name, day, lastRefresh).
In the repository, we have the getCity (city_name, day) method that will fetch a city forecast for a given day and, if the data is more than 1 minute in the database, a refresh is made to the weather forecast data of this city on this day.
The refresh is done using the IPMA API. To obtain wind and weather classification in descriptive terms, the IPMA API (with different requests) is also used. Since APIs were not found to provide continuous meteorological data alarms, an extra API (worldweatheronline) was used to provide average temperature and rainfall data in the current month for a given city.
Finally, in relation to additional libraries (that is, in addition to the obligatory libraries), Dagger 2, Retrofit, GSON, Glide, ButterKnife and Location / Maps were used.

# 3-LIMITATIONS

Sometimes the API that provides the average data for the month does not send a response (this can even be verified by testing on their site). Often, closing the app and re-opening works soon but other times it may take some time. However, when the data is received, it works as expected.