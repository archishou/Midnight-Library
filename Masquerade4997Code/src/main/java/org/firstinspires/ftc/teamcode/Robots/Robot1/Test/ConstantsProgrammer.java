package org.firstinspires.ftc.teamcode.Robots.Robot1.Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Library4997.MasqRobot;
import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archishmaan Peyyety on 10/20/18.
 * Project: MasqLib
 */
@TeleOp(name = "ConstantsProgrammer", group = "NFS")
public class ConstantsProgrammer extends MasqLinearOpMode {
    private double dumper = 0;
    MasqRobot robot;
    @Override
    public void runLinearOpMode() throws InterruptedException {
        while (!opModeIsActive()) {
            dash.create("HELLO ");
            dash.update();
        }
        waitForStart();
        while (opModeIsActive()) {
            if (controller1.xOnPress()) dumper += 0.01;
            if (controller1.yOnPress()) dumper -= 0.01;
            controller1.update();
        }
    }
}
