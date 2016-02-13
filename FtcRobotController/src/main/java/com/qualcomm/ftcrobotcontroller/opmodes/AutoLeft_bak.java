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

public class AutoLeft_bak extends OpMode {

    DcMotor frontrightMotor;
    DcMotor frontleftMotor;
    DcMotor backrightMotor;
    DcMotor backleftMotor;
    Servo climberD;
    Servo button;
    TouchSensor touchBeacon;
    ColorSensor colorBeacon;

    int step = 1;

    @Override
    public void init() {
        frontleftMotor = hardwareMap.dcMotor.get("frontLeft");
        frontrightMotor = hardwareMap.dcMotor.get("frontRight");
        backleftMotor = hardwareMap.dcMotor.get("backLeft");
        backrightMotor = hardwareMap.dcMotor.get("backRight");

        climberD = hardwareMap.servo.get("climberD");
        button = hardwareMap.servo.get("button");

        frontleftMotor.setDirection(DcMotor.Direction.REVERSE);
        backleftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontrightMotor.setDirection(DcMotor.Direction.FORWARD);
        backrightMotor.setDirection(DcMotor.Direction.FORWARD);

        backleftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backrightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontleftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontrightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

    }

    @Override
    public void start() {
        backleftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backrightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        backleftMotor.setPower(1.0);
        backrightMotor.setPower(1.0);

        backleftMotor.setTargetPosition(0);
    }

    @Override
    public void loop() {

        switch (step){
            case(1):moveRobot(24,"f");break;
            case(2):moveRobot(12,"b");break;
            case(3):moveRobot(24,"l");break;
            case(4):moveRobot(12,"r");break;
            default:break;
        }

    }

    @Override
    public void stop() {

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
            leftPower = 1.0;
            rightPower = 1.0;
            rightCount = COUNTS;
            leftCount = COUNTS;
        } else if (direction.equals("b")) {
            leftPower = -1.0;
            rightPower = -1.0;
            rightCount = -COUNTS;
            leftCount = -COUNTS;
        } else if (direction.equals("l")) {
            leftPower = -1.0;
            rightPower = 1.0;
            rightCount = COUNTS;
            leftCount = -COUNTS;
        } else if (direction.equals("r")) {
            leftPower = 1.0;
            rightPower = -1.0;
            rightCount = -COUNTS;
            leftCount = COUNTS;
        } else {
        }

        backleftMotor.setTargetPosition((int) leftCount);
        backrightMotor.setTargetPosition((int) rightCount);

        backleftMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backrightMotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);


        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Left Position", backleftMotor.getCurrentPosition());
        telemetry.addData("Right Position", backrightMotor.getCurrentPosition());
        telemetry.addData("Step",step);
//        telemetry.addData("climberD", "climberD:  " + String.format("%.2f", climberDPosition));
//        telemetry.addData("button", "button:  " + String.format("%.2f", buttonPosition));

        if  ((Math.abs(backleftMotor.getCurrentPosition())/(0.001+Math.abs(backleftMotor.getTargetPosition()))*100 < 95)&&
                (Math.abs(backrightMotor.getCurrentPosition())/(0.001+Math.abs(backrightMotor.getTargetPosition()))*100 <95)) {
            frontleftMotor.setPower(leftPower);
            frontrightMotor.setPower(rightPower);
        }
        else {
            frontleftMotor.setPowerFloat();
            frontrightMotor.setPowerFloat();
            step++;
            backleftMotor.setTargetPosition(0);
            backrightMotor.setTargetPosition(0);
            backleftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            backrightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ie) {
                //Handle exception
            }
        }

    }


}