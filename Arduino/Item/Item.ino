#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// --- Pins ---
const int greenLed = 5;  // Green LED
const int redLed = 4;    // Red LED

// --- LCD Setup ---
LiquidCrystal_I2C lcd(0x27, 16, 2);  // Change address if needed
String lcdBuffer = "";                // Store incoming text

void setup() {
  Serial.begin(115200);       // Must match Python baud rate
  pinMode(greenLed, OUTPUT);
  pinMode(redLed, OUTPUT);

  // Ensure LEDs start off
  digitalWrite(greenLed, LOW);
  digitalWrite(redLed, LOW);

  // Initialize LCD
  lcd.init();
  lcd.backlight();
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Ready...");
}

void loop() {
  while (Serial.available() > 0) {
    char incoming = Serial.read();

    // LED control
    if (incoming == 'G') {
      digitalWrite(greenLed, HIGH);
      digitalWrite(redLed, LOW);
      delay(500);
      digitalWrite(greenLed, LOW);
    } 
    else if (incoming == 'R') {
      digitalWrite(redLed, HIGH);
      digitalWrite(greenLed, LOW);
      delay(500);
      digitalWrite(redLed, LOW);
    } 
    else if (incoming != '\n' && incoming != '\r') {
      // Accumulate characters for LCD display
      lcdBuffer += incoming;
    }
    else if (incoming == '\n') {
      // End of text, print price only
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Price:");
      lcd.setCursor(0, 1);
      lcd.print(lcdBuffer);  // show price on second line
      lcdBuffer = "";        // clear buffer
    }
  }
}
