package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Matthew Hotham on 12/22/2015.
 */
public class StateMachineMatt extends OpMode {

    final static double climberD_MIN_RANGE = 0.00;
    final static double climberD_MAX_RANGE = 1.00;
    final static double button_MIN_RANGE = 0.30;
    final static double button_MAX_RANGE = 0.60;

    double climberDPosition;
    double buttonPosition;

    double red;
    double blue;

    int first;

    DcMotor FrontRight;
    DcMotor FrontLeft;
    DcMotor BackRight;
    DcMotor BackLeft;

    Servo climberD;
    Servo button;

    TouchSensor touchBeacon;
    ColorSensor colorBeacon;

    double climberDDelta = 0.1;
    double buttonDelta = 0.1;

    enum State {first, second, third, fourth, fifth, sixth, seventh, eighth, ninth}

    State state;

    int ENCODER_CPR = 1440;
    double GEAR_RATIO = 2;
    int WHEEL_DIAMETER = 4;
    final int Distance = 1;    //  move 1 inch
    final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final double ROTATIONS = Distance / CIRCUMFERENCE;
    final double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO; // counts to move 1 inch (multiply by needed value)

    @Override
    public void init() {

        FrontLeft = hardwareMap.dcMotor.get("frontLeft");
        FrontRight = hardwareMap.dcMotor.get("frontRight");
        BackLeft = hardwareMap.dcMotor.get("backLeft");
        BackRight = hardwareMap.dcMotor.get("backRight");

        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);

        BackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        BackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        FrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        FrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        colorBeacon = hardwareMap.colorSensor.get("colorBeacon");

        climberDPosition = 0.80;
        buttonPosition = 0.45;

        state = State.first;
    }

    @Override
    public void loop() {

        colorBeacon.enableLed(false);

        switch (state) {
            case first:

                FrontLeft.setTargetPosition(0);
                FrontRight.setTargetPosition(0);
                BackRight.setTargetPosition(0);
                BackLeft.setTargetPosition(0);

                BackLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                BackRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                FrontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
                FrontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

                BackLeft.setPower(1.0);
                FrontLeft.setPower(1.0);
                BackRight.setPower(1.0);
                FrontRight.setPower(1.0);


            case second:


        }
    }
}








