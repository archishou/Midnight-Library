package org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.MarkOneFoundationHook;
import org.firstinspires.ftc.teamcode.SkystoneDetection.DogeDetector;

import Library4997.MasqControlSystems.MasqPurePursuit.MasqPositionTracker;
import Library4997.MasqDriveTrains.MasqMechanumDriveTrain;
import Library4997.MasqMotors.MasqMotor;
import Library4997.MasqMotors.MasqMotorSystem;
import Library4997.MasqResources.MasqHelpers.MasqMotorModel;
import Library4997.MasqRobot;
import Library4997.MasqServos.MasqServo;

/**
 * Created by Archishmaan Peyyety on 2019-08-06.
 * Project: MasqLib
 */
public class MarkOne extends MasqRobot {

    public MasqServo blockGrabber, blockRotater, blockPusher, sideGrabber;
    public MarkOneFoundationHook foundationHook;
    public MasqMotor  lift;
    public MasqMotorSystem intake;
    public DogeDetector detector;

    @Override
    public void init(HardwareMap hardwareMap) {
        driveTrain = new MasqMechanumDriveTrain(hardwareMap);
        blockGrabber = new MasqServo("blockGrabber", hardwareMap);
        lift = new MasqMotor("lift", MasqMotorModel.ORBITAL20, hardwareMap);
        blockRotater = new MasqServo("blockRotater", hardwareMap);
        intake = new MasqMotorSystem("intakeRight", DcMotorSimple.Direction.FORWARD, "intakeLeft", DcMotorSimple.Direction.REVERSE,MasqMotorModel.REVHDHEX40, hardwareMap);
        blockPusher = new MasqServo("blockPusher", hardwareMap);
        imu = initializeIMU(hardwareMap);
        tracker = new MasqPositionTracker(lift, intake.motor1, imu); //Replace motors when odometry is incorporating
        foundationHook = new MarkOneFoundationHook(hardwareMap);
        //detector = new DogeDetector(DogeDetector.Cam.PHONE, hardwareMap);
        //sideGrabber = new MasqServo("sideGrabber", hardwareMap);

        blockPusher.scaleRange(0, 0.5);
        blockGrabber.scaleRange(0, 0.5);
        blockRotater.scaleRange(0.02, 0.7);
        //sideGrabber.scaleRange(0, 1);
        lift.encoder.setWheelDiameter(1);
    }

    public void resetServos() {
        blockPusher.setPosition(0);
        blockRotater.setPosition(0);
        blockGrabber.setPosition(1);
        foundationHook.lower();
        sideGrabber.setPosition(0);
    }

    public void runStoneLeft(HardwareMap hardwareMap) {
        //Collect and place on foundation first and fourth blocks
    }
    public void runStoneMiddle(HardwareMap hardwareMap) {
        //Collect and place on foundation second and fifth blocks
    }
    public void runStoneRight(HardwareMap hardwareMap) {
        //Collect and place on foundation third and sixth blocks
    }


}