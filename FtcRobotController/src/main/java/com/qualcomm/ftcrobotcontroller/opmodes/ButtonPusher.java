package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Matthew Hotham on 12/16/2015.
 */
public class ButtonPusher extends OpMode {

    final static double climberD_MIN_RANGE = 0.00;
    final static double climberD_MAX_RANGE = 1.00;
    final static double button_MIN_RANGE = 0.3;
    final static double button_MAX_RANGE = 0.6;

    ColorSensor colorBeacon;
    TouchSensor touchBeacon;

    Servo climberD;
    Servo button;

    double climberDPosition;
    double climberDDelta = 0.01;

    double buttonPosition;
    double buttonDelta = 0.1;

    int red;
    int blue;

    public ButtonPusher() {

    }

    public void init() {

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        colorBeacon = hardwareMap.colorSensor.get("colorBeacon");
        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        climberDPosition = 1.0;
        buttonPosition = 0.45;

    }

    @Override
    public void loop() {

        colorBeacon.enableLed(false);

        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
        telemetry.addData("Red  ", colorBeacon.red());
        telemetry.addData("Blue ", colorBeacon.blue());

        climberD.setPosition(climberDPosition);
        button.setPosition(buttonPosition);

        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        buttonPosition = Range.clip(buttonPosition, button_MIN_RANGE, button_MAX_RANGE);

        if (touchBeacon.isPressed()) {

            red = (colorBeacon.red());
            blue = (colorBeacon.blue());

            if (red > blue) {
               climberDPosition -= climberDDelta;
                buttonPosition += buttonDelta;
            }
            if (red < blue) {
               climberDPosition -= climberDDelta;
                buttonPosition -= buttonDelta;
            }

        }
    }
}
