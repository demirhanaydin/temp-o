#!/usr/bin/python
import sys

import Adafruit_DHT
# import subprocess
import os
from string import Template
from Adafruit_CharLCD import Adafruit_CharLCD


# Parse command line parameters.
SENSOR_ARGS = { '11': Adafruit_DHT.DHT11,
				'22': Adafruit_DHT.DHT22,
				'2302': Adafruit_DHT.AM2302 }
# if len(sys.argv) == 3 and sys.argv[1] in sensor_args:
# 	sensor = sensor_args[sys.argv[1]]
# 	pin = sys.argv[2]
# else:
# 	print 'usage: sudo ./Adafruit_DHT.py [11|22|2302] GPIOpin#'
# 	print 'example: sudo ./Adafruit_DHT.py 2302 4 - Read from an AM2302 connected to GPIO #4'
# 	sys.exit(1)

# UUID
# a6bff840-f11a-11e4-a9f6-0002a5d5c51b

# OVERRIDE VARIABLES
SENSOR = SENSOR_ARGS['11']
PIN = 4
LCD = Adafruit_CharLCD()
LCD.begin(16,1)

def advertise_temperature(temperature, humidity):
  code = Template('sudo hcitool -i hci0 cmd 0x08 0x0008 1E 02 01 1A 1A FF 4C 00 A6 BF F8 40 F1 1A 11 E4 A9 F6 00 02 A5 D5 C5 1B $hex_str C5')
  adv_code = code.substitute(hex_str = temp_to_hex(temperature, humidity))
  print adv_code
  # subprocess.call(adv_code)
  os.system(adv_code)

def prettify_str(str):
  pretty = ''
  for char in str:
    pretty+=char.capitalize()
    if len(pretty)%3 == 2:
      pretty+=' '
  return pretty.strip()

def temp_to_hex(temperature, humidity):
  return prettify_str((str(temperature) + ' '+ str(humidity)).encode('hex'))

def print_to_lcd(message):
  LCD.clear()
  LCD.message(message)

def print_temp():
  # set initial variables
  humidity, temperature = (None, None)
  while True:
    # remove this line after being sure about all is well
    # humidity, temperature = (None, None)
    # Try to grab a sensor reading.  Use the read_retry method which will retry up
    # to 15 times to get a sensor reading (waiting 2 seconds between each retry).
    new_humidity, new_temperature = Adafruit_DHT.read_retry(SENSOR, PIN)

    # Note that sometimes you won't get a reading and
    # the results will be null (because Linux can't
    # guarantee the timing of calls to read the sensor).  
    # If this happens try again!
    if new_humidity is not None and new_temperature is not None:
    	# print 'Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity)
      if new_humidity != humidity and new_temperature != temperature:
        output = 'Temp={0:0.1f}*C \nHumidity={1:0.1f}%'.format(new_temperature, new_humidity)
        print output
        print_to_lcd(output)
        advertise_temperature(new_temperature, int(new_humidity))
        humidity, temperature = (new_humidity, new_temperature)
    else:
    	print 'Failed to get reading. Try again!'

print_temp()