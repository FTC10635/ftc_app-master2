package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Matthew Hotham on 11/4/2015.
 */
public class testDriveTrainMatt extends OpMode {

    // TETRIX VALUES.
//    climber delivery system
    final static double climberD_MIN_RANGE  = 0.00;
    final static double climberD_MAX_RANGE  = 1.00;
//    climber release left
    final static double climberR_MIN_RANGE  = 0.10;
    final static double climberR_MAX_RANGE  = 0.68;
//    button presser
    final static double button_MIN_RANGE  = 0.30;
    final static double button_MAX_RANGE  = 0.60;
//    climber release right
    final static double climberRR_MIN_RANGE = 0.20;
    final static double climberRR_MAX_RANGE = 0.78;


    // position of the servo
    double climberDPosition;

    // amount to change the servo position
    double climberDDelta = 0.01;

    double climberRPosition;

    double climberRDelta = 1.0;

    double buttonPosition;

    double buttonDelta = 0.01;

    double climberRRPosition;

    double climberRRDelta = 1.0;


    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor motorslideM;
    Servo climberD;
    Servo climberR;
    Servo button;
    Servo climberRR;
    TouchSensor touchBeacon;

    /**
     * Constructor
     */
    public testDriveTrainMatt() {

    }

//      Code to run when the op mode is first enabled goes here

//      @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()

    @Override
    public void init() {

		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorslideM = hardwareMap.dcMotor.get("slideM");

        climberD = hardwareMap.servo.get("climberD");
        climberR = hardwareMap.servo.get("climberR");
        button = hardwareMap.servo.get("button");
        climberRR = hardwareMap.servo.get("climberRR");

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");

        // assign the starting position of the servos
        climberDPosition = 1.0;
        climberRPosition = 0.68;
        buttonPosition = 0.45;
        climberRRPosition = 0.20;
    }

//     * This method will be called repeatedly in a loop

//     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()

    @Override
    public void loop() {

		/*
		 * Gamepad 1 controls the slide motor via left stick
		 *
		 * Gamepad 1 controls the motors via the left and right sticks, and it controls the
		 * servos via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;
        float slide = -gamepad1.left_stick_x;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        slide = Range.clip(slide, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);
        slide = (float)scaleInput(slide);

        // write the values to the motors
        motorFrontRight.setPower(right);
        motorFrontLeft.setPower(left);
        motorBackRight.setPower(right);
        motorBackLeft.setPower(left);
        motorslideM.setPower(slide);

        // update the position of the servo.
        if (gamepad1.a) {
           climberDPosition += climberDDelta;
        }
        if (gamepad1.x) {
            climberRRPosition -= climberRRDelta;
        }
        if (gamepad1.dpad_up) {
            climberRPosition -= climberRDelta;
        }
        if (gamepad1.dpad_left) {
           buttonPosition -= buttonDelta;
        }
        if (gamepad1.dpad_right) {
           buttonPosition += buttonDelta;
        }
        if (gamepad1.b) {
            climberDPosition -= climberDDelta;
        }
        if (gamepad1.y) {
            climberRRPosition += climberRRDelta;
        }
        if (gamepad1.dpad_down) {
            climberRPosition += climberRDelta;
        }

        // clip the position values so that they never exceed their allowed range.
        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        buttonPosition = Range.clip(buttonPosition, button_MIN_RANGE, button_MAX_RANGE);
        climberRPosition = Range.clip(climberRPosition, climberR_MIN_RANGE, climberR_MAX_RANGE);
        climberRRPosition = Range.clip(climberRRPosition, climberRR_MIN_RANGE, climberRR_MAX_RANGE);

        // write position values to the servos
        climberD.setPosition(climberDPosition);
        button.setPosition(buttonPosition);
        climberRR.setPosition(climberRRPosition);
        climberR.setPosition(climberRPosition);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

 //       telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));
        telemetry.addData("climberR", "climberR:  " + String.format("%.2f", climberRPosition));
        telemetry.addData("climberRR", "climberRR:  " + String.format("%.2f", climberRRPosition));
        telemetry.addData("left tgt pwr",  "left  pwr:  " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr:  " + String.format("%.2f", right));
        telemetry.addData("slide tgt pwr", "slide pwr:  " + String.format("%.2f", slide));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
    }

//     * Code to run when the op mode is first disabled goes here

//     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()

    @Override
    public void stop() {

    }

//     * This method scales the joystick input so for low joystick values, the
//     * scaled value is less than linear. This is to make it easier to drive
//     * the robot more precisely at slower speeds.

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}

