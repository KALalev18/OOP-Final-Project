# OOP API and JSON example

## Description

This application connects to Statistics Finland API and fetches population information from Finnish municipalities.

Jackson libraty is used in parsing the JSON data and making the API calls. See pom.xml for how Jackson-library is included ine project

Jackson documentation: https://github.com/FasterXML/jackson-docs

Statistics Finland APIs: https://pxdata.stat.fi/PxWeb/pxweb/en/StatFin/


## How to use Statistics Finland API endpoints?

The endpoint used in fetching population information is: https://pxdata.stat.fi/PxWeb/pxweb/en/StatFin/StatFin__synt/statfin_synt_pxt_12dy.px/

Go to the web page above.

In the information section select "Population", in the year-selection "Select all"

Select one municipality, for example "Lahti". Click show table

Scroll down to the bottom of the page, and expand "API query fore this table". 

Click "Save API query (json)". This information is savd to query.json file in this project.

KU398 is the municipality code for Lahti. Municipality codes KUXXX are used when searching data for municipalities via the API.

The JSON mapping of codes to municipality names you can get from copying the URL in the URL field in the "API query for yhis table" -section. It is https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px


This application builds a map between municipality name and the municipality code, so that when you enter the name of the municipality, it will know which municipality code it will pass to the other API queries.


## Translations for values in Statistics Finland API

Unfortunately Statistics Finland has some information hardcoded in Finnish

Here is the dictionary for selected terms:

Alue = area, meaning the municipality

Tiedot = information

vaesto =  population (note that the correct spelling is väestö, but to avoid encoding issues, the Nordic letters are not used) 

Vuosi = year






