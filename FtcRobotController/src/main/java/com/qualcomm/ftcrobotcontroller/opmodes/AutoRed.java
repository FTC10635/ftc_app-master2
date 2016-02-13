package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by Ben Hardin on 12/11/2015.
 */

public class AutoRed extends OpMode {

    final static double climberD_MIN_RANGE = 0.00;
    final static double climberD_MAX_RANGE = 1.00;

    final static double button_MIN_RANGE = 0.28;
    final static double button_MAX_RANGE = 0.62;

    double climberDPosition;
    double buttonPosition;

    DcMotor backRight;
    DcMotor backLeft;
    DcMotor frontRight;
    DcMotor frontLeft;

    Servo button;

    ColorSensor colorBeacon;
    TouchSensor touchBeacon;

    Servo climberD;

    double buttonDelta = 0.1;

    double red;
    double blue;

    int step = 1;

    @Override
    public void init() {

        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");

        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        colorBeacon = hardwareMap.colorSensor.get("colorBeacon");

        climberDPosition = 0.80;
        buttonPosition = 0.45;

    }

    @Override
    public void start() {
        backLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        backLeft.setPower(1.0);
        frontLeft.setPower(1.0);
        backRight.setPower(1.0);
        frontRight.setPower(1.0);

        backLeft.setTargetPosition(0);
        frontLeft.setTargetPosition(0);
    }

    @Override
    public void loop() {

        switch (step){
            case(1):moveRobot(4,"f");break;
            case(2):moveRobot(10,"l");break;
            case(3):moveRobot(113,"f");break;
            case(4):moveRobot(17,"b");break;
            case(5):moveRobot(10,"l");break;
            case(6):moveRobot(26, "f");break;
            case(9):ButtonPusher();break;
            default:break;
        }

    }

    @Override
    public void stop() {

    }

    public void ButtonPusher() {

        colorBeacon.enableLed(false);

        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:   " + String.format("%.2f", buttonPosition));
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

            if (red > blue + 0.1) {
                climberDPosition = 0;
                buttonPosition -= buttonDelta;
            }
            if (red + 0.1 < blue) {
                climberDPosition = 0;
                buttonPosition += buttonDelta;
            }
        }
    }
    public void moveRobot(int driveDistance, String direction) {

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

        backLeft.setTargetPosition((int) leftCount);
        backRight.setTargetPosition((int) rightCount);
        frontLeft.setTargetPosition((int) leftCount);
        frontRight.setTargetPosition((int) rightCount);


        backLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);


        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Back Left Position", backLeft.getCurrentPosition());
        telemetry.addData("Back Right Position", backRight.getCurrentPosition());
        telemetry.addData("Step", step);
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));
        telemetry.addData("Red  ", colorBeacon.red());
        telemetry.addData("Blue ", colorBeacon.blue());


        if  ((Math.abs(backLeft.getCurrentPosition())/(0.001+Math.abs(backLeft.getTargetPosition()))*100 > 95)&&
                (Math.abs(backRight.getCurrentPosition())/(0.001+Math.abs(backRight.getTargetPosition()))*100 > 95)) {
            step++;

            frontLeft.setTargetPosition(0);
            frontRight.setTargetPosition(0);
            backLeft.setTargetPosition(0);
            backRight.setTargetPosition(0);

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

        if(touchBeacon.isPressed()) {

            frontLeft.setTargetPosition(0);
            backLeft.setTargetPosition(0);
            backRight.setTargetPosition(0);
            frontRight.setTargetPosition(0);

            frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            step = 9;
        }

    }

}