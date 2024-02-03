#!/bin/bash

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is not installed. Please install jq before running the job."
    echo "On Debian/Ubuntu, you can install it using: sudo apt-get install jq"
    exit 1
fi

# Repeatedly call the job
while true; do
    echo -n "Populating METAR storage: "
    date

    ./populate_metar_storage.sh

    sleep 5
done
