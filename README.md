# temp-o
Local temperature measurement system with Raspberry Pi

## Components
- Rapsberry pi
- DHT11 temp & humidity sensor (or equivalent)
- Bluetooth 4.0 Dongle
- Standard LCD 16x2 Screen (optional)
- Android (>= 5.1.0 / API 22)

## Libraries
- https://github.com/adafruit/Adafruit_Python_DHT
- https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code

## Install
- Wire LCD with following [the tutorial](https://learn.adafruit.com/drive-a-16x2-lcd-directly-with-a-raspberry-pi?view=all)
- Wire temperature sensor with following [the tutorial](https://learn.adafruit.com/dht-humidity-sensing-on-raspberry-pi-with-gdocs-logging?view=all)
- Setup bluetooth with following [the tutorial](https://learn.adafruit.com/pibeacon-ibeacon-with-a-raspberry-pi?view=all)
- Run the script `sudo /home/pi/examples/Adafruit_Python_DHT/examples/broadcastTempData.py`
- Install android app which is located '/TempSystem' folder

## Screenshots
