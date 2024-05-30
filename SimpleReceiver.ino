#include <Arduino.h>
#include <ArduinoBLE.h>
#include "ArduinoGraphics.h"
#include "Arduino_LED_Matrix.h"

#define MACHINE_TYPE_UUID "FD2DE127-D3EA-4385-A2BC-CEE9EFB1531D" // For the jukebox type
#define SONG_NUMBER_UUID "25CDD283-218B-44E2-927A-4EC194351E4F" // For the song number received
#define JUKEBOX_SERVICE_UUID "4bcdc221-276e-4117-bd83-34bd4bc9d003" //

ArduinoLEDMatrix matrix;
BLEService jukeboxService(JUKEBOX_SERVICE_UUID);
BLECharacteristic machineTypeCharacteristic(MACHINE_TYPE_UUID, BLERead | BLENotify, 1); // For the jukebox type
BLECharacteristic songNumberCharacteristic(SONG_NUMBER_UUID, BLERead | BLEWrite | BLENotify, 20);

// For testing
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
String receivedValue ="";

void characteristicWritten(BLEDevice central, BLECharacteristic characteristic) {
  if (characteristic == songNumberCharacteristic) {
    if (songNumberCharacteristic.written()) {
      uint8_t buffer[20]; // adjust buffer size as per your requirement
      int len = songNumberCharacteristic.readValue(buffer, sizeof(buffer));

      Serial.print("Received song number: ");

      // Convert byte array to string
      receivedValue = "";
      for (int i = 0; i < len; i++) {
        Serial.print(buffer[i], HEX); // Print hex values for debugging
        receivedValue += (char)buffer[i];
      }
      Serial.println();

      Serial.print("Received song number (as string): ");
      Serial.println(receivedValue);
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

  // Add characteristics to the service 
  jukeboxService.addCharacteristic(machineTypeCharacteristic); 
  jukeboxService.addCharacteristic(songNumberCharacteristic);

  // Add service to BLE
  BLE.addService(jukeboxService);

  // Set event handler for when the song number characteristic is written
  songNumberCharacteristic.setEventHandler(BLEWritten, characteristicWritten);
  
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
      if (!receivedValue.isEmpty()) {
        matrix.clear();
        matrix.beginDraw();
        matrix.stroke(0xFFFFFFFF); // Set the stroke color to white
        matrix.textScrollSpeed(30); // Set the text scroll speed
        matrix.textFont(Font_5x7); // Set the font
        
        // Begin drawing text
        matrix.beginText(0, 1, 0xFFFFFF); // Set text starting position and color
        matrix.println("Received song:"); // Print the prefix text
        matrix.println(receivedValue); // Print the received song number
        matrix.endText(SCROLL_LEFT); // End text and set scroll direction
        
        matrix.endDraw(); // End drawing on the matrix

        delay(50); // Delay to allow for the text to be displayed

        receivedValue = ""; // Clear the received value after displaying it
      }
      delay(10);
    }
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  } else {
    BLE.advertise();
  }
}
