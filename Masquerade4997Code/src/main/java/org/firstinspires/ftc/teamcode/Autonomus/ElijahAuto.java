package org.firstinspires.ftc.teamcode.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import Library4997.MasqLinearOpMode;
import Library4997.MasqRobot.Direction;

/**
 * This is a basic template copy and paste this class for any auto,
 * refactor the file name to match the auto class title
 */

@Autonomous(name = "ElijahAuto", group = "Template")
public class ElijahAuto extends MasqLinearOpMode implements Constants {
    public void runLinearOpMode() throws InterruptedException {
        while (!opModeIsActive()) {
            dash.create(robot.imu);
            dash.update();
        }
        waitForStart();
        robot.drive(40, POWER_OPTIMAL);
        robot.shooter.setPower(TARGET_POWER);
        robot.indexer.setPosition(INDEXER_OPENED);
        robot.sleep(500);
        robot.indexer.setPosition(INDEXER_CLOSED);
        robot.sleep(500);
        robot.indexer.setPosition(INDEXER_OPENED);
        robot.turn(30,Direction.RIGHT);
        robot.drive(10);
    }
}
