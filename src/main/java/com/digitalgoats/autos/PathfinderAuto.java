package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class PathfinderAuto extends Auto {

  EncoderFollower left, right;

  public PathfinderAuto(SystemGroup systems) {

    super("Pathfinder Auto", systems);

    Trajectory main = Pathfinder.generate(new Waypoint[] {
        new Waypoint(0, 0, 0),
        new Waypoint(100, 0, 90)
    }, new Config(
        FitMethod.HERMITE_CUBIC,
        Config.SAMPLES_HIGH,
        .05,
        100,
        200,
        600
    ));
    TankModifier tank = new TankModifier(main).modify((23.5)/12);

    left = new EncoderFollower(tank.getLeftTrajectory());
    right = new EncoderFollower(tank.getRightTrajectory());

  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        left.configureEncoder((int)this.getSystems().drive.getLeftPosition(), 1440, .5);
        right.configureEncoder((int)this.getSystems().drive.getRightPosition(), 1440, .5);
        left.configurePIDVA(1.5, 0, 0, 1/100, 0);
        right.configurePIDVA(1.5, 0, 0, 1/100, 0);
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.nextStep();
        break;
      }

      case 1: {
        double leftOutput = left.calculate((int)this.getSystems().drive.getLeftPosition())/100;
        double rightOutput = right.calculate((int)this.getSystems().drive.getRightPosition())/100;
        double deltaAngle = Pathfinder.boundHalfDegrees(Pathfinder.r2d(left.getHeading()) - this.getSystems().gyro.getAngle());
        double turn = .8 * (-1/80) * deltaAngle;
        this.getSystems().drive.setLeftSetPoint(leftOutput + turn);
        this.getSystems().drive.setRightSetPoint(rightOutput - turn);
        SmartDashboard.putNumber("Left Output", leftOutput);
        SmartDashboard.putNumber("Right Output", rightOutput);
        SmartDashboard.putNumber("Turn", turn);
        break;
      }

    }
  }

}
