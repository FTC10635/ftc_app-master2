package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Matthew Hotham on 12/22/2015.
 */
public class MattAuto2 extends OpMode {

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

    Servo climberD;
    Servo button;

    TouchSensor touchBeacon;
    ColorSensor colorBeacon;

    final static double climberD_MIN_RANGE = 0.00;
    final static double climberD_MAX_RANGE = 1.00;
    final static double button_MIN_RANGE = 0.30;
    final static double button_MAX_RANGE = 0.60;

    double climberDPosition;
    double buttonPosition;

    double red;
    double blue;

    double climberDDelta = 0.1;
    double buttonDelta = 0.1;


    @Override
    public void init() {

        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);

        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        colorBeacon = hardwareMap.colorSensor.get("colorBeacon");

    }




    @Override
    public void loop() {


        {

            motorFrontLeft.setPower(-1);
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(-1);
            motorFrontRight.setPower(-1);

//            Thread.sleep(1000);

            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);

//            Thread.sleep(50);

            motorFrontLeft.setPower(1);
            motorBackLeft.setPower(1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);

//            Thread.sleep(500);

            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);

//            Thread.sleep(50);

            motorBackLeft.setPower(-1);
            motorFrontLeft.setPower(-1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);

//            Thread.sleep(200);

            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);

//            Thread.sleep(50);

            motorFrontLeft.setPower(-1);
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(-1);
            motorFrontRight.setPower(-1);

//            Thread.sleep(10000);

            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);

//            Thread.sleep(50);

            motorFrontLeft.setPower(1);
            motorBackLeft.setPower(1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);

//            Thread.sleep(200);

            motorFrontLeft.setPower(-1);
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);

//            Thread.sleep(200);

            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);

//            Thread.sleep(50);

            motorFrontLeft.setPower(-1);
            motorBackLeft.setPower(-1);
            motorBackRight.setPower(-1);
            motorFrontRight.setPower(-1);




        }


    }

}



