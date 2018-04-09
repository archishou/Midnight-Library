package org.firstinspires.ftc.teamcode.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Library4997.MasqUtilities.Direction;
import Library4997.MasqUtilities.MasqUtils;
import Library4997.MasqUtilities.StopCondition;
import Library4997.MasqUtilities.Strafe;
import Library4997.MasqWrappers.MasqLinearOpMode;
import SubSystems4997.SubSystems.Flipper;
import SubSystems4997.SubSystems.Gripper.Grip;

/**
 * Created by Archish on 3/8/18.
 */
@Autonomous(name = "RedAutoSingleV3", group = "A")
public class RedAutoSingle extends MasqLinearOpMode implements Constants {
    double startTicks = 0, endTicks = 0;
    boolean secondCollection = false;
    public void runLinearOpMode() throws InterruptedException {
        robot.mapHardware(hardwareMap);
        robot.lineDetector.setMargin(20);
        robot.vuforia.initVuforia(hardwareMap);
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        robot.initializeAutonomous();
        robot.initializeServos();
        robot.intake.motor1.setStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(OUTAKE);
            }
        });
        robot.intake.motor1.setUnStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(INTAKE);
            }
        });
        robot.gripper.setGripperPosition(Grip.CLAMP);
        robot.flipper.setFlipperPosition(Flipper.Position.MID);
        while (!opModeIsActive()) {
            dash.create("Stalled: ");
            dash.update();
        }
        waitForStart();
        robot.intake.setPower(INTAKE);
        robot.intake.motor1.enableStallDetection();
        robot.vuforia.flash(true);
        robot.sleep(robot.getDelay());
        robot.vuforia.activateVuMark();
        String vuMark = readVuMark();
        runJewel();
        robot.vuforia.flash(false);
        robot.driveTrain.setClosedLoop(false);
        runVuMark(vuMark);
    }
    public void runJewel() {
        robot.jewelArmRed.setPosition(JEWEL_RED_OUT);
        robot.sleep(1000);
        if (robot.jewelColorRed.isRed()) robot.redRotator.setPosition(ROTATOR_RED_SEEN);
        else robot.redRotator.setPosition(ROTATOR_RED_NOT_SEEN);
        robot.sleep(250);
        robot.jewelArmRed.setPosition(JEWEL_RED_IN);
        robot.sleep(100);
    }
    public String readVuMark () {
        robot.waitForVuMark();
        return robot.vuforia.getVuMark();
    }
    public void runVuMark(String vuMark) {
        robot.intake.setPower(INTAKE);
        robot.gripper.setGripperPosition(Grip.CLAMP);
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        if (MasqUtils.VuMark.isCenter(vuMark)) {
            robot.drive(22, POWER_OPTIMAL, Direction.BACKWARD, 3);
            scoreRightFirstDump();
        }
        else if (MasqUtils.VuMark.isLeft(vuMark)) {
            robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD, 2); //FINAL
            scoreRightFirstDump();
        }
        else if (MasqUtils.VuMark.isRight(vuMark)) {
            robot.drive(8, POWER_OPTIMAL, Direction.BACKWARD, 2);}
        else if (MasqUtils.VuMark.isUnKnown(vuMark)) {
            robot.drive(22, POWER_OPTIMAL, Direction.BACKWARD, 3);
            scoreRightFirstDump();
        }
    }
    public void scoreRightFirstDump() {
        robot.turnAbsolute(60, Direction.RIGHT);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        robot.gripper.setGripperPosition(Grip.OUT);
        robot.drive(4, POWER_OPTIMAL, Direction.BACKWARD);
        robot.drive(5);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.turnAbsolute(90, Direction.RIGHT);
        startTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.doubleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        if (robot.doubleBlock.stop()) {
            secondCollection = true;
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.strafe(12, Strafe.LEFT);
            robot.stop(new StopCondition() {
                @Override
                public boolean stop() {
                    return robot.doubleBlock.stop();
                }
            }, -90, POWER_LOW, Direction.FORWARD);
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.strafe(12, Strafe.RIGHT);
            robot.turnAbsolute(90, Direction.RIGHT);
        }
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                if (secondCollection) {
                    robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD);
                }
                else {
                    robot.turnAbsolute(85, Direction.RIGHT);
                    robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.gripper.setGripperPosition(Grip.CLAMP);
                robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            }
        });
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
                robot.drive(10, POWER_OPTIMAL, Direction.FORWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(500);
                robot.gripper.setGripperPosition(Grip.OUT);
            }
        });
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.strafe(20, Strafe.RIGHT);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.doubleBlock.stop();
            }
        }, -90, POWER_LOW, Direction.FORWARD);
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        if (robot.doubleBlock.stop()) {
            secondCollection = true;
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.strafe(12, Strafe.LEFT);
            robot.stop(new StopCondition() {
                @Override
                public boolean stop() {
                    return robot.doubleBlock.stop();
                }
            }, -90, POWER_LOW, Direction.FORWARD);
            robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
            robot.strafe(12, Strafe.RIGHT);
            robot.turnAbsolute(90, Direction.RIGHT);
        }
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                if (secondCollection) {
                    robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD);
                }
                else {
                    robot.turnAbsolute(85, Direction.RIGHT);
                    robot.drive(30, POWER_OPTIMAL, Direction.BACKWARD);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.gripper.setGripperPosition(Grip.CLAMP);
                robot.flipper.setFlipperPosition(Flipper.Position.OUT);
            }
        });
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                robot.drive(5, POWER_OPTIMAL, Direction.BACKWARD);
                robot.drive(10, POWER_OPTIMAL, Direction.FORWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(500);
                robot.gripper.setGripperPosition(Grip.OUT);
            }
        });

    }
}