package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Elevator;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import java.io.File;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(Drive.COUNT_FOOT * 1);
        this.getSystems().drive.setRightSetPoint(Drive.COUNT_FOOT * 1);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        }
        break;
      }

      case 2: {
        break;
      }

    }
  }

}
