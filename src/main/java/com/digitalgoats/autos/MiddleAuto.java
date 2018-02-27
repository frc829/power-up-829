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
import jaci.pathfinder.modifiers.TankModifier;
import java.io.File;
import java.nio.file.Path;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  EncoderFollower elevatorFollower, leftFollower, rightFollower;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(12 * Drive.COUNT_INCH);
        this.getSystems().drive.setRightSetPoint(12 * Drive.COUNT_INCH);
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.nextStep();
        }
        break;
      }

    }
  }

}
