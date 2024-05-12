#include <Arduino.h>
#include <ArduinoBLE.h>
#include "Arduino_LED_Matrix.h"

#define MACHINE_TYPE_UUID "FD2DE127-D3EA-4385-A2BC-CEE9EFB1531D" // For the jukebox type
#define SONG_NUMBER_UUID "25CDD283-218B-44E2-927A-4EC194351E4F" // For the song number received

ArduinoLEDMatrix matrix;
BLECharacteristic machineTypeCharacteristic(MACHINE_TYPE_UUID, BLERead, 1); // For the jukebox type
BLECharacteristic songNumberCharacteristic(SONG_NUMBER_UUID, BLEWrite, 1);

const uint32_t happy[] = {
    0x19819,
    0x80000001,
    0x81f8000
};
const uint32_t heart[] = {
    0x3184a444,
    0x44042081,
    0x100a0040
};

bool deviceConnected = false;

void setup() {
  Serial.begin(9600); // initialize serial communication
  matrix.begin();

  if (!BLE.begin()) {
      Serial.println("starting BLE failed!");
      while (1);
  }

  BLECharacteristic placeholderCharacteristic(0x180A, BLERead, 20); // Device Information Service UUID
  placeholderCharacteristic.setValue("Placeholder");

  BLE.setLocalName("Jukebox Receiver");
  BLEService jukeboxService = BLEService();

  machineTypeCharacteristic.setValue(0); // Default machine type  
  jukeboxService.addCharacteristic(machineTypeCharacteristic); 
 
  songNumberCharacteristic.setValue(0);
  jukeboxService.addCharacteristic(songNumberCharacteristic);

  BLE.addService(jukeboxService);
  BLE.advertise();
  Serial.println("Bluetooth device active, waiting for connections...");
}

void loop() {
  BLEDevice central = BLE.central(); // wait for a central

  if (central) {
    Serial.print("Connected to central: ");
    Serial.println(central.address());

    // Represents record or CD machine, sends the info
    uint8_t machineType = 1;
    machineTypeCharacteristic.writeValue(&machineType, sizeof(machineType));
  
    while (central.connected()) {
      if (songNumberCharacteristic.written()) {
        // Song number received as a byte
        uint8_t receivedSong = machineTypeCharacteristic.value()[0];
        Serial.print("Received song number: ");
        Serial.println(receivedSong);

        // TODO encode numbers to LED light
        matrix.loadFrame(receivedSong == 1 ? happy : heart);
        delay(500);
      }
    }
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  } else {
    BLE.advertise();
  }
}
