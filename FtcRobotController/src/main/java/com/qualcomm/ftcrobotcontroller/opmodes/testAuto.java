package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

/**
 * Created by Ben Hardin on 12/3/2015.
 */
public class testAuto extends LinearOpMode {
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;


    @Override
    public void runOpMode() throws InterruptedException {
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        for(int i=0; i<1; i++) {
            motorBackLeft.setPower(1.0);
            motorBackRight.setPower(1.0);
            motorFrontLeft.setPower(1.0);
            motorFrontRight.setPower(1.0);
            sleep(1000);
        }
        motorFrontLeft.setPowerFloat();
        motorFrontRight.setPowerFloat();
        motorBackRight.setPowerFloat();
        motorBackLeft.setPowerFloat();
    }

}
