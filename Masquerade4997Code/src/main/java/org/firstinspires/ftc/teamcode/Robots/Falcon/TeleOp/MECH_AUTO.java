package org.firstinspires.ftc.teamcode.Robots.Falcon.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robots.Falcon.Falcon;

import Library4997.MasqResources.MasqHelpers.Direction;
import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archishmaan Peyyety on 10/27/18.
 * Project: MasqLib
 */
@TeleOp(name = "MECH_AUTO", group = "NFS")
public class MECH_AUTO extends MasqLinearOpMode implements Constants {
    private Falcon falcon = new Falcon();
    @Override
    public void runLinearOpMode()  {
        falcon.setStartOpenCV(false);
        falcon.mapHardware(hardwareMap);
        falcon.initializeTeleop();
        falcon.hang.setClosedLoop(true);
        falcon.driveTrain.setClosedLoop(true);
        falcon.dumper.setPosition(DUMPER_IN);
        while (!opModeIsActive()) {
            dash.create("Hello");
            dash.update();
        }
        waitForStart();
        while (opModeIsActive()) {
            falcon.MECH(controller1, Direction.FORWARD, true);

            if (controller1.leftBumper()) falcon.collector.setPower(.5);
            else if (controller1.leftTriggerPressed()) falcon.collector.setPower(-.5);
            else falcon.collector.setPower(0);

            if (controller2.b()) falcon.dumper.setPosition(DUMPER_OUT);
            else falcon.dumper.setPosition(DUMPER_IN);

            if (controller2.leftStickY() < 0 && falcon.rotateTopLimit.isPressed()) falcon.hang.setPower(-1);
            else if (controller2.leftStickY() > 0 && falcon.rotateDownLimit.isPressed()) falcon.hang.setPower(1);
            else falcon.hang.setBreakMode();

            falcon.rotator.DriverControl(controller2);
            falcon.lift.DriverControl(controller1);
            falcon.tracker.updateSystem();
            dash.create("X: ", falcon.tracker.getGlobalX());
            dash.create("Y: ", falcon.tracker.getGlobalY());
            dash.create("H: ", falcon.tracker.getHeading());
            dash.create("ADJ(H): ", falcon.tracker.imu.adjustAngle(falcon.tracker.getHeading()));
            dash.update();
            controller1.update();
        }
    }
}