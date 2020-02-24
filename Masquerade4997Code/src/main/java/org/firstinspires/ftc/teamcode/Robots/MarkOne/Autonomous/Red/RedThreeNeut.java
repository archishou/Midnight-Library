package org.firstinspires.ftc.teamcode.Robots.MarkOne.Autonomous.Red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.MarkOne;
import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter;

import java.util.ArrayList;
import java.util.List;

import Library4997.MasqControlSystems.MasqPurePursuit.MasqWayPoint;
import Library4997.MasqWrappers.MasqLinearOpMode;

import static Library4997.MasqControlSystems.MasqPurePursuit.MasqWayPoint.PointMode.MECH;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition.MIDDLE;
import static org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.CVInterpreter.SkystonePosition.RIGHT;

/**
 * Created by Keval Kataria on 1/4/2020
 */
@Autonomous(name = "RedThreeNeut", group = "MarkOne")
public class RedThreeNeut extends MasqLinearOpMode {
    private MarkOne robot = new MarkOne();
    private SkystonePosition position;
    private List<MasqWayPoint> stones = new ArrayList<>();
    private MasqWayPoint
            bridge1 = new MasqWayPoint().setPoint(24, 20, 90).setSwitchMode(MECH),
            bridge2 = new MasqWayPoint().setPoint(59, 25, 90).setSwitchMode(MECH).setOnComplete(() -> {
                robot.sideGrabber.leftClose(0);
                robot.sideGrabber.leftUp(0);
            }),
            park = new MasqWayPoint().setPoint(35,24, 90).setMaxVelocity(0.5).setMinVelocity(0),
            foundationOne = new MasqWayPoint().setPoint(86, 32, 90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.leftSlightClose(0);
                robot.sideGrabber.leftLowMid(0);
            }),
            foundationTwo = new MasqWayPoint().setPoint(88, 32, 90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.leftSlightClose(0);
                robot.sideGrabber.leftLowMid(0);
            }),
            foundationThree = new MasqWayPoint().setPoint(90, 32, 90).setTargetRadius(3).setMinVelocity(0).setOnComplete(() -> {
                robot.sideGrabber.leftSlightClose(0);
                robot.sideGrabber.leftLowMid(0);
            });

    @Override
    public void runLinearOpMode() {
        robot.init(hardwareMap);
        robot.initializeAutonomous();
        robot.initCamera(hardwareMap);

        stones.add(null);

        stones.add(new MasqWayPoint().setPoint(15, 29, 90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
/**/    stones.add(new MasqWayPoint().setPoint(8, 29, 90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
/**/    stones.add(new MasqWayPoint().setPoint(0, 29, 90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));

        stones.add(new MasqWayPoint().setPoint(-9, 29,90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
        stones.add(new MasqWayPoint().setPoint(-17, 29, 90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2));
/**/    stones.add(new MasqWayPoint().setPoint(-25, 29, 90).setMinVelocity(0).setTargetRadius(0.5).setModeSwitchRadius(2).setDriveCorrectionSpeed(0.04));

        while (!opModeIsActive()) {
            position = CVInterpreter.getRed(robot.cv.detector);
            dash.create("Skystone Position: " + position);
            dash.update();
        }

        waitForStart();

        timeoutClock.reset();
        robot.sideGrabber.leftDown(0);
        robot.sideGrabber.rightUp(0);
        robot.sideGrabber.leftOpen(0);
        robot.sideGrabber.rightClose(0);
        robot.foundationHook.raise();

        if (position == RIGHT) runSimultaneously(
                () -> mainAuto(stones.get(1), stones.get(4),stones.get(3)),
                () -> robot.cv.stop()
        );
        else if (position == MIDDLE) runSimultaneously(
                () -> mainAuto(stones.get(2), stones.get(5),stones.get(3)),
                () -> robot.cv.stop()
        );
        else runSimultaneously(
                () -> mainAuto(stones.get(3), stones.get(6),stones.get(2)),
                () -> robot.cv.stop()
        );
    }

    private void mainAuto(MasqWayPoint stone1, MasqWayPoint stone2, MasqWayPoint stone3) {
        grabStone(stone1.setSwitchMode(MECH).setOnComplete(() -> {
            robot.sideGrabber.leftClose(1);
            robot.sideGrabber.leftUp(0.5);
        }), foundationOne,true);
        robot.tracker.setDrift(-2, 3);
        grabStone(stone2, foundationTwo,false);
        robot.tracker.setDrift(-2, 6);
        grabStone(stone3, foundationThree,false);
        foundationPark();
    }

    private void grabStone(MasqWayPoint stone, MasqWayPoint foundation, boolean firstStone) {
        if (firstStone) robot.xyPath(4, stone);
        else robot.xyPath(9, bridge2, bridge1.setOnComplete(() -> {
            robot.sideGrabber.leftOpen(0);
            robot.sideGrabber.leftDown(0);
        }), stone.setOnComplete(() -> {
            double closeSleep = 1, rotateSleep = 0.5;
            robot.sideGrabber.leftClose(closeSleep);
            robot.sideGrabber.leftUp(rotateSleep);
        }));
        robot.driveTrain.setVelocity(0);
        robot.xyPath(5, bridge1.setOnComplete(null), bridge2, foundation);
        robot.driveTrain.setVelocity(0);
    }

    private void foundationPark() {
        robot.xyPath(29 - timeoutClock.seconds(), bridge2.setMinVelocity(0.5), park);
        robot.stop(29 - timeoutClock.seconds());
        //obot.drive(60, Direction.BACKWARD);
    }
}