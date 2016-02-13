package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Ben Hardin on 12/11/2015.
 */

public class AutoBlue_test extends OpMode {

    // set the position of each servo as a variable
    double climberDPosition;
    double buttonPosition;

//  calls all motors, servos, and sensors
    DcMotor backRight;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor frontLeft;

    Servo button;
    Servo climberD;
    Servo climberR;
    Servo climberRR;

    ColorSensor colorBeacon;
    TouchSensor touchBeacon;

    // assign variables for red and blue color levels
    double red;
    double blue;

//  creates variables that are used to control the movement of the wheels and servos later
    int step = 1;
    int push = 0;

    //  Code to run when the op mode is set to INIT mode
    @Override
    public void init() {

        // assign the motors by name
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");

        // assign the servos by name
        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");
        climberR = hardwareMap.servo.get("climberR");
        climberRR = hardwareMap.servo.get("climberRR");

        // set the directions of the motors
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // reset the encoders to 0
        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        // assign the sensors by name and type
        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        colorBeacon = hardwareMap.colorSensor.get("colorBeacon");

//      sets the starting positions of the servos
        buttonPosition = 0.45;
        climberDPosition = 1.0;

    }

    @Override
    public void start() {

        backLeft.setPower(1.0);
        frontLeft.setPower(1.0);
        backRight.setPower(1.0);
        frontRight.setPower(1.0);

    }

    @Override
    public void loop() {
//      switch statement created for the movements
        switch (step){
            case(1):moveRobot(24.6,"f");break;
            case(2):moveRobot(10,"r");break;
            case(3):moveRobot(83,"f");break;
            case(4):moveRobot(17,"b");break;
            case(5):moveRobot(10.8,"r");break;
            case(6):moveRobot(30, "f");break;
            case(9):ButtonPusher();break;
            default:break;

        }

    }

    @Override
    public void stop() {

    }

    public void ButtonPusher() {

        // turn off the Led on the color sensor
        colorBeacon.enableLed(false);

        // Send telemetry data back to driver station
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:   " + String.format("%.2f", buttonPosition));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
        telemetry.addData("Red  ", colorBeacon.red());
        telemetry.addData("Blue ", colorBeacon.blue());
//      tells the robot what to do when the touch sensor is pressed
        if (touchBeacon.isPressed()) {
//          scans the beacon to determine the color
            red = (colorBeacon.red());
            blue = (colorBeacon.blue());
//          tells the servo what side to press and dumps the climbers in the bucket
            if (red > blue + 0.1 && push < 1) {
                climberDPosition = 0;
                buttonPosition = 0.62;
                climberD.setPosition(climberDPosition);
                button.setPosition(buttonPosition);
                push = 1;
            }
            if (red + 0.1 < blue && push < 1) {
                climberDPosition = 0;
                buttonPosition = 0.28;
                climberD.setPosition(climberDPosition);
                button.setPosition(buttonPosition);
                push = 1;
            }
        }
    }


//  all the code used to set up the encoders and movements
    public void moveRobot(double driveDistance, String direction) {

        int ENCODER_CPR = 1440;     //Encoder Counts per Revolution
        double GEAR_RATIO = 2;      //Gear Ratio
        int WHEEL_DIAMETER = 4;     //Diameter of the wheel in inches
        double leftPower = 1.0;
        double rightPower = 1.0;
        final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
        final double ROTATIONS = driveDistance / CIRCUMFERENCE;
        final double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;
        double rightCount = 0.01;
        double leftCount = 0.01;

        if (direction.equals("f")) {
            rightCount = COUNTS;
            leftCount = COUNTS;
        } else if (direction.equals("b")) {
            rightCount = -COUNTS;
            leftCount = -COUNTS;
        } else if (direction.equals("r")) {
            rightCount = COUNTS;
            leftCount = -COUNTS;
        } else if (direction.equals("l")) {
            rightCount = -COUNTS;
            leftCount = COUNTS;
        } else {
        }

//      sets the starting positions of the servos
        climberD.setPosition(climberDPosition);
        button.setPosition(buttonPosition);
        climberR.setPosition(0.82);
        climberRR.setPosition(0.19);

//      sets the direction of the wheels
        backLeft.setTargetPosition((int) leftCount);
        backRight.setTargetPosition((int) rightCount);
        frontLeft.setTargetPosition((int) leftCount);
        frontRight.setTargetPosition((int) rightCount);

//      primes the robot for movement
        backLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        // Send telemetry data back to driver station
        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Back Left Position", backLeft.getCurrentPosition());
        telemetry.addData("Back Right Position", backRight.getCurrentPosition());
        telemetry.addData("Step", step);
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));
        telemetry.addData("Red  ", colorBeacon.red());
        telemetry.addData("Blue ", colorBeacon.blue());


//      makes it so the program can advance the switch statement after moving
        if  ((Math.abs(backLeft.getCurrentPosition())/(0.001+Math.abs(backLeft.getTargetPosition()))*100 > 95)&&
                (Math.abs(backRight.getCurrentPosition())/(0.001+Math.abs(backRight.getTargetPosition()))*100 > 95)) {

            step++;

            // reset the encoders to 0
            backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            try {
                Thread.sleep(2500);
            }   catch (InterruptedException ie) {
                //Handle exception
            }
        }

//      stops robot movement if the touch sensor is pressed
        if(touchBeacon.isPressed()) {

            // reset the encoders to 0
            frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            step = 9;

        }

    }

}