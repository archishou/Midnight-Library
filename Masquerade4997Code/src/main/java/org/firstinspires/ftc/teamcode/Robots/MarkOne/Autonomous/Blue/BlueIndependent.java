package org.firstinspires.ftc.teamcode.Robots.MarkOne.Autonomous.Blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.MarkOne;
import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter;

import java.util.ArrayList;
import java.util.List;

import Library4997.MasqControlSystems.MasqPurePursuit.MasqWayPoint;
import Library4997.MasqWrappers.MasqLinearOpMode;

import static Library4997.MasqControlSystems.MasqPurePursuit.MasqWayPoint.PointMode.MECH;
import static Library4997.MasqResources.MasqHelpers.Direction.BACKWARD;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition.LEFT;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition.MIDDLE;

/**
 * Created by Keval Kataria on 1/4/2020
 */
@Autonomous(name = "BlueIndependent", group = "MarkOne")
public class BlueIndependent extends MasqLinearOpMode {
    private MarkOne robot = new MarkOne();
    private SkystonePosition position;
    private List<MasqWayPoint> stones = new ArrayList<>();
    private MasqWayPoint
            bridge1 = new MasqWayPoint().setPoint(-24, 20, -90).setSwitchMode(MECH),
            bridge2 = new MasqWayPoint().setPoint(-59, 25, -90).setSwitchMode(MECH).setOnComplete(() -> {
                robot.sideGrabber.rightClose(0);
                robot.sideGrabber.rightUp(0);
            }),
            foundationOne = new MasqWayPoint().setPoint(-86, 32, -90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.rightSlightClose(0);
                robot.sideGrabber.rightLowMid(0);
            }),
            foundationTwo = new MasqWayPoint().setPoint(-88, 32, -90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.rightSlightClose(0);
                robot.sideGrabber.rightLowMid(0);
            }),
            foundationThree = new MasqWayPoint().setPoint(-92, 32, -90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.rightLowMid(0);
                robot.sideGrabber.rightOpen(1);
            });

    @Override
    public void runLinearOpMode() {
        robot.init(hardwareMap);
        robot.initializeAutonomous();
        robot.initCamera(hardwareMap);

        stones.add(null);

        stones.add(new MasqWayPoint().setPoint(-15, 29.5, -90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
        stones.add(new MasqWayPoint().setPoint(-8, 29.5, -90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
        stones.add(new MasqWayPoint().setPoint(0, 29.5, -90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));

        stones.add(new MasqWayPoint().setPoint(9, 29.5,-90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
        stones.add(new MasqWayPoint().setPoint(15, 29.5, -90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
        stones.add(new MasqWayPoint().setPoint(25, 29.5, -90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2).setDriveCorrectionSpeed(0.04));

        while (!opModeIsActive()) {
            position = CVInterpreter.getBlue(robot.cv.detector);
            dash.create("Skystone Position: " + position);
            dash.update();
        }

        waitForStart();

        timeoutClock.reset();
        robot.sideGrabber.rightDown(0);
        robot.sideGrabber.leftUp(0);
        robot.sideGrabber.rightOpen(0);
        robot.sideGrabber.leftClose(0);
        robot.foundationHook.raise();

        if (position == LEFT) runSimultaneously(
                () -> mainAuto(stones.get(1), stones.get(4),stones.get(3)),
                () -> robot.cv.stop()
        );
        else if (position == MIDDLE) runSimultaneously(
                () -> mainAuto(stones.get(2), stones.get(5),stones.get(3)),
                () -> robot.cv.stop()
        );
        else runSimultaneously(
                () -> mainAuto(stones.get(3), stones.get(6),stones.get(1)),
                () -> robot.cv.stop()
        );
    }

    private void mainAuto(MasqWayPoint stone1, MasqWayPoint stone2, MasqWayPoint stone3) {
        grabStone(stone1.setSwitchMode(MECH).setOnComplete(() -> {
            robot.sideGrabber.rightClose(1);
            robot.sideGrabber.rightUp(0.5);
        }), foundationOne,true);
        robot.tracker.setDrift(0, -3);
        grabStone(stone2, foundationTwo,false);
        robot.tracker.setDrift(0, -6);
        grabStone(stone3, foundationThree,false);
        foundationPark();
    }

    private void grabStone(MasqWayPoint stone, MasqWayPoint foundation, boolean firstStone) {
        if (firstStone) robot.xyPath(4, stone);
        else robot.xyPath(9, bridge2, bridge1.setOnComplete(() -> {
            robot.sideGrabber.rightOpen(0);
            robot.sideGrabber.rightDown(0);
        }), stone.setOnComplete(() -> {
            double closeSleep = 1, rotateSleep = 0.5;
            robot.sideGrabber.rightClose(closeSleep);
            robot.sideGrabber.rightUp(rotateSleep);
        }));
        robot.driveTrain.setVelocity(0);
        robot.xyPath(5, bridge1.setOnComplete(null), bridge2, foundation);
        robot.driveTrain.setVelocity(0);
    }

    private void foundationPark() {
        robot.turnAbsolute(178,1);
        robot.drive(5,1,BACKWARD,1);
        robot.foundationHook.lower();
        sleep();
        MasqWayPoint p1 = new MasqWayPoint().setPoint(-60,0, 90)
                .setDriveCorrectionSpeed(0.2).setMinVelocity(0);
        MasqWayPoint p2 = new MasqWayPoint().setPoint(-90,20, 60)
                .setDriveCorrectionSpeed(1).setTimeout(1).setSwitchMode(MECH).setOnComplete(() -> robot.foundationHook.raise());
        MasqWayPoint p3 = new MasqWayPoint().setPoint(-45,28, 90)
                .setDriveCorrectionSpeed(0.2).setLookAhead(5);
        robot.xyPath(5, p1, p2, p3);
        robot.stop(0.5);
    }
}