#include <Arduino.h>
#include <ArduinoBLE.h>
#include "Arduino_LED_Matrix.h"

#define MACHINE_TYPE_UUID "FD2DE127-D3EA-4385-A2BC-CEE9EFB1531D" // For the jukebox type
#define SONG_NUMBER_UUID "25CDD283-218B-44E2-927A-4EC194351E4F" // For the song number received
#define JUKEBOX_SERVICE_UUID "4bcdc221-276e-4117-bd83-34bd4bc9d003" //

ArduinoLEDMatrix matrix;
BLEService jukeboxService(JUKEBOX_SERVICE_UUID);
BLECharacteristic machineTypeCharacteristic(MACHINE_TYPE_UUID, BLERead | BLENotify, 1); // For the jukebox type
BLEUnsignedCharCharacteristic songNumberCharacteristic(SONG_NUMBER_UUID, BLERead | BLENotify);

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


void characteristicWritten(BLEDevice central, BLECharacteristic characteristic) {
  if (characteristic == songNumberCharacteristic) {
    if (songNumberCharacteristic.written()) {
      uint8_t buffer[20]; // adjust buffer size as per your requirement
      int len = songNumberCharacteristic.readValue(buffer, sizeof(buffer));

      // Convert byte array to a string
      String receivedValue = "";
      for (int i = 0; i < len; i++) {
        receivedValue += (char)buffer[i];
      }

      Serial.print("Received song number: ");
      Serial.println(receivedValue);

      // TODO: Encode numbers to LED light
      // matrix.loadFrame(value == "1" ? happy : heart);
      // delay(500);
    }
  }
}

void setup() {
  Serial.begin(9600); // initialize serial communication
  matrix.begin();

  if (!BLE.begin()) {
      Serial.println("starting BLE failed!");
      while (1);
  }

  BLE.setLocalName("Jukebox Receiver");
  BLE.setAdvertisedService(jukeboxService);

  //machineTypeCharacteristic.setValue(0); // Default machine type  
  jukeboxService.addCharacteristic(machineTypeCharacteristic); 
 
  //songNumberCharacteristic.setValue(0);


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
    uint8_t machineType = rand() % 2;
    Serial.print("Sent machine type ");
    Serial.println(machineType);
    machineTypeCharacteristic.writeValue(&machineType, sizeof(machineType));
  
    while (central.connected()) {
      
      if (songNumberCharacteristic.written()) {
        matrix.loadFrame(happy);
        if (songNumberCharacteristic.value()) {
          Serial.print("Received song number: ");
          matrix.loadFrame(happy);
        } else {
          //matrix.loadFrame(heart);
        }

        // TODO encode numbers to LED light
        //matrix.loadFrame(value == 1 ? happy : heart);
        delay(500);
      }
    }
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  } else {
    BLE.advertise();
  }
}
