package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Matthew Hotham on 11/4/2015.
 */
public class TeleOp extends OpMode {

//  state the min and max ranges for each servo
    final static double climberD_MIN_RANGE  = 0.00;
    final static double climberD_MAX_RANGE  = 1.00;

    final static double climberR_MIN_RANGE  = 0.00;
    final static double climberR_MAX_RANGE  = 0.78;

    final static double climberRR_MIN_RANGE = 0.20;
    final static double climberRR_MAX_RANGE = 1.00;

    // set the position of each servo as a variable
    double climberDPosition;
    double buttonPosition;
    double climberRPosition;
    double climberRRPosition;

    // set the amount to change the servo position each hardware cycle
    double climberDDelta = 0.01;
    double climberRDelta = 1.0;
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


    public TeleOp() {

    }

//  Code to run when the op mode is set to INIT mode
    @Override
    public void init() {

        // assign the motors by name
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorslideM = hardwareMap.dcMotor.get("slideM");

        // set the direction of the motors
        motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorslideM.setDirection(DcMotor.Direction.FORWARD);

        // assign the servos by name
        climberD = hardwareMap.servo.get("climberD");
        climberR = hardwareMap.servo.get("climberR");
        button = hardwareMap.servo.get("button");
        climberRR = hardwareMap.servo.get("climberRR");

        // assign the starting positions of the servos
        climberDPosition = 1.0;
        climberRPosition = 0.78;
        buttonPosition = 0.45;
        climberRRPosition = 0.20;
    }

    @Override
    public void loop() {

		/*
		 * Gamepad 1 controls the the drive motors via the left and right sticks
		 *
		 * Gamepad 2 controls the slide motor via the left stick and controls the servos using up, down, a, b, x and y
		 */

        // write the joystick values to variables
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;
        float slide = -gamepad2.left_stick_y;

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

        // update the position of the servos based on gamepad inputs
        if (gamepad2.x) {
           climberDPosition += climberDDelta;
        }
        if (gamepad2.a) {
            climberRRPosition -= climberRRDelta;
        }
        if (gamepad2.dpad_up) {
            climberRPosition -= climberRDelta;
        }
        if (gamepad2.y) {
            climberDPosition -= climberDDelta;
        }
        if (gamepad2.b) {
            climberRRPosition += climberRRDelta;
        }
        if (gamepad2.dpad_down) {
            climberRPosition += climberRDelta;
        }

        // clip the position values of the servos so that they never exceed their allowed range.
        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        climberRPosition = Range.clip(climberRPosition, climberR_MIN_RANGE, climberR_MAX_RANGE);
        climberRRPosition = Range.clip(climberRRPosition, climberRR_MIN_RANGE, climberRR_MAX_RANGE);

        // write the position values to the servos
        climberD.setPosition(climberDPosition);
        button.setPosition(buttonPosition);
        climberRR.setPosition(climberRRPosition);
        climberR.setPosition(climberRPosition);

        // Send telemetry data back to driver station
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("climberR", "climberR:  " + String.format("%.2f", climberRPosition));
        telemetry.addData("climberRR", "climberRR:  " + String.format("%.2f", climberRRPosition));
        telemetry.addData("left tgt pwr",  "left  pwr:  " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr:  " + String.format("%.2f", right));
        telemetry.addData("slide tgt pwr", "slide pwr:  " + String.format("%.2f", slide));

    }

//     * Code to run when the op mode is first disabled goes here
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

