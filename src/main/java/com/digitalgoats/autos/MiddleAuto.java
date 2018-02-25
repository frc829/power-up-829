package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;
import java.io.File;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  File leftCsv, rightCsv;

  EncoderFollower leftFollower, rightFollower;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      // Load trajectory from file and configure encoder followers
      case 0: {

        if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT) {
          leftCsv = new File("/home/lvuser/traj/middle/left-left.csv");
          rightCsv = new File("/home/lvuser/traj/middle/left-right.csv");
        } else {
          leftCsv = new File("/home/lvuser/traj/middle/right-left.csv");
          rightCsv = new File("/home/lvuser/traj/middle/right-right.csv");
        }

        leftFollower = new EncoderFollower(Pathfinder.readFromCSV(leftCsv));
        rightFollower = new EncoderFollower(Pathfinder.readFromCSV(rightCsv));

        leftFollower.configureEncoder(
            (int)this.getSystems().drive.getLeftPosition(),
            Drive.ENC_COUNTS,
            Drive.WHEEL_DIAMETER
        );
        leftFollower.configurePIDVA(0, 0, 0, 1/1.7, 0);
        rightFollower.configureEncoder(
            (int)this.getSystems().drive.getRightPosition(),
            Drive.ENC_COUNTS,
            Drive.WHEEL_DIAMETER
        );
        rightFollower.configurePIDVA(0, 0, 0, 1/1.7, 0);

        this.nextStep();

        break;
      }

      // Execute trajectory
      case 1: {

        double left = leftFollower.calculate((int)this.getSystems().drive.getLeftPosition());
        double right = rightFollower.calculate((int)this.getSystems().drive.getRightPosition());
        double gyroHeading = this.getSystems().gyro.getAngle();
        double desiredHeading = Pathfinder.r2d(leftFollower.getHeading());
        double deltaHeading = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = .8 * (-1/80) * deltaHeading;

        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setLeftSetPoint(left + turn);
        this.getSystems().drive.setRightSetPoint(right - turn);

        break;
      }

    }
  }

}
