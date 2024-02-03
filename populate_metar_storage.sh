#!/bin/bash

METAR_STORAGE_BASE_URL="http://localhost:8080"
METAR_SERVICE_URL="https://tgftp.nws.noaa.gov/data/observations/metar/stations/"
METAR_ENDPOINT_TEMPLATE="${METAR_STORAGE_BASE_URL}/airport/{airport_code}/METAR"
SUBSCRIPTION_ENDPOINT="${METAR_STORAGE_BASE_URL}/subscriptions"

# Function to fetch and store METAR data for a given airport code
fetch_and_store_metar() {
    airport_code="$1"
    metar_endpoint=$(echo "${METAR_ENDPOINT_TEMPLATE}" | sed "s/{airport_code}/${airport_code}/g")

    fetched=$(curl -s "${METAR_SERVICE_URL}/${airport_code}.TXT")
    datetime=$(echo $fetched | awk '{print $1, $2}')
    data=$(echo $fetched | awk '{$1=$2=$3=""; print $0}' | awk '{$1=$1};1')

    response=$(curl -s -X POST -H "Content-Type: application/json" -d "{\"icaoCode\": \"${icaoCode}\", \"data\": \"${data}\", \"timestamp\": \"${timestamp}\"}" "${metar_endpoint}" 2>&1)
    echo "METAR storage response: ${response}"
}

# Function to fetch all subscribed airports and call fetch_and_store_metar for each
fetch_and_store_all_metar() {
    subscribed_airports=$(curl -s "${SUBSCRIPTION_ENDPOINT}")

    jq -r '.subscriptions[] | .icaoCode' <<< "${subscribed_airports}" | while read -r icaoCode; do
        fetch_and_store_metar "${icaoCode}"
    done
}

fetch_and_store_all_metar