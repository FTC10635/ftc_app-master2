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

public class AutoRight3 extends OpMode {

    final static double climberD_MIN_RANGE  = 0.00;
    final static double climberD_MAX_RANGE  = 1.00;
    DcMotor rightMotor;
    DcMotor leftMotor;
    Servo button;
    ColorSensor colorBeacon;
    TouchSensor touchBeacon;
    Servo climberD;

    double climberDPosition;
    double climberDDelta = 0.01;



    int step = 1;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");

        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        touchBeacon = hardwareMap.touchSensor.get("touchBeacon");
        climberD = hardwareMap.servo.get("climberD");
        climberDPosition = 1.0;

    }

    @Override
    public void start() {
        leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(1.0);
        rightMotor.setPower(1.0);

        leftMotor.setTargetPosition(0);
    }

    @Override
    public void loop() {

        switch (step){
            case(1):moveRobot(126,"f");break;
            case(2):moveRobot(23,"b");break;
            case(3):moveRobot(10,"l");break;
            case(4):moveRobot(26,"f");break;
            case(9):launchClimbers();break;
            default:break;
        }

    }

    @Override
    public void stop() {

    }

    public void launchClimbers() {
        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
        telemetry.addData("isPressed", String.valueOf(touchBeacon.isPressed()));
        climberD.setPosition(climberDPosition);
        climberDPosition = Range.clip(climberDPosition, climberD_MIN_RANGE, climberD_MAX_RANGE);
        climberDPosition -= climberDDelta;
        
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

        leftMotor.setTargetPosition((int) leftCount);
        rightMotor.setTargetPosition((int) rightCount);

        leftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Back Left Position", leftMotor.getCurrentPosition());
        telemetry.addData("Back Right Position", rightMotor.getCurrentPosition());
        telemetry.addData("Step", step);
//        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
//        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));


        if  ((Math.abs(leftMotor.getCurrentPosition())/(0.001+Math.abs(leftMotor.getTargetPosition()))*100 > 95)&&
                (Math.abs(rightMotor.getCurrentPosition())/(0.001+Math.abs(rightMotor.getTargetPosition()))*100 > 95)) {
            step++;
            leftMotor.setTargetPosition(0);
            rightMotor.setTargetPosition(0);
            leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            try {
                Thread.sleep(3000);
            }   catch (InterruptedException ie) {
                //Handle exception
            }
        }

        if(touchBeacon.isPressed()) {
            leftMotor.setTargetPosition(0);
            rightMotor.setTargetPosition(0);
            leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            step = 9;
        }


    }

}