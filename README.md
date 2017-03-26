## Overview

This is an Android Studio reference app for the AndroidBeaconLibrary supporting AltBeacon compatible devices.
It has been cloned from the source at <https://altbeacon.github.io/android-beacon-library/samples.html>

For demonstration, the parser layouts have been edited to work with Eddystone beacons.
A Raspberry Pi may be configured as a beacon as follows (Assuming Bluetooth available on the Pi)
- To generate a command for an Eddystone URL beacon, enter the URL here in the calculator here: <http://yencarnacion.github.io/eddystone-url-calculator/>
- It will generate the commands you need to run the pi as a beacon
- You may need to follow these commands with the following command: *sudo hcitool cmd 0x08 0x000A 01*
