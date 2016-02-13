package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Cameron on 12/15/2015.
 */
public class launchClimbers extends OpMode {


    final static double climberD_MIN_RANGE  = 0.02;
    final static double climberD_MAX_RANGE  = 1.00;

    TouchSensor touchBeacon;
    Servo climberD;

    double climberDPosition;
    double climberDDelta = 0.01;

    public launchClimbers() {

    }

    public void init() {
        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        climberD = hardwareMap.servo.get("climberD");
        climberDPosition = 1.0;
    }

    @Override
    public void loop() {
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
        climberD.setPosition(climberDPosition);
        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        if(touchBeacon.isPressed()) {
            climberDPosition -= climberDDelta;
        }
    }
}
