package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

//import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Ben Hardin on 12/7/2015.
 */
public class testDriveTrainFinal extends OpMode {


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
    Servo climberD;
    DcMotor motorSlideM;
    //    Servo climberR;
    Servo button;
    TouchSensor touchBeacon;
    Servo climberR;
    Servo climberRR;

    /**
     * Constructor
     */
    public testDriveTrainFinal() {

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

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorSlideM = hardwareMap.dcMotor.get("slideM");
        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotor.Direction.FORWARD);


        climberD = hardwareMap.servo.get("climberD");
        climberR = hardwareMap.servo.get("climberR");
        button = hardwareMap.servo.get("button");
        climberRR = hardwareMap.servo.get("climberRR");


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
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left1 = -gamepad1.left_stick_y;
        float right1 = -gamepad1.right_stick_y;
        float left2 = -gamepad2.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        right1 = Range.clip(right1, -1, 1);
        left1 = Range.clip(left1, -1, 1);
        left2 = Range.clip(left2, -1, 1);
        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right1 = (float)scaleInput(right1);
        left1 =  (float)scaleInput(left1);
        left2 = (float)scaleInput(left2);
        // write the values to the motors
        motorFrontRight.setPower(right1);
        motorFrontLeft.setPower(left1);
        motorBackRight.setPower(right1);
        motorBackLeft.setPower(left1);
        motorSlideM.setPower(left2);
        // update the position of the arm.
        if (gamepad2.a) {
//             if the A button is pushed on gamepad1, increment the position of
//             the climberD servo.
            climberDPosition += climberDDelta;
        }



        // update the position of the servo
        if (gamepad2.dpad_left) {
            buttonPosition += buttonDelta;
        }

        if (gamepad2.dpad_right) {
            buttonPosition -= buttonDelta;
        }

        if (gamepad2.b) {
            climberDPosition -= climberDDelta;
        }


        // update the position of the servo





        // clip the position values so that they never exceed their allowed range.
        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        buttonPosition = Range.clip(buttonPosition, button_MIN_RANGE, button_MAX_RANGE);
        climberRPosition = Range.clip(climberRPosition, climberR_MIN_RANGE, climberR_MAX_RANGE);
        // write position values to the wrist and claw servo
        climberD.setPosition(climberDPosition);
        button.setPosition(buttonPosition);
        climberR.setPosition(climberRPosition);

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        //       telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:   " + String.format("%.2f", buttonPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left1));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right1));
        telemetry.addData("left tgt pwr", "left pwr: " + String.format("%.2f", left2));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
    }

//     * Code to run when the op mode is first disabled goes here

//     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()

    @Override
    public void stop() {

    }

//     * This method scales the joystick input so for low joystick values, the
//     * scaled value is less than linear.  This is to make it easier to drive
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