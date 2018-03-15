package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Elevator;
import com.digitalgoats.systems.Manipulator.PivotPosition;
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

  double angle = 0, distance = 0;
  long lastTime = 0;
  int step = 0;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  public long getDeltaTime() {
    return System.currentTimeMillis() - lastTime;
  }

  @Override
  public void execute() {
    System.out.println(this.getStep());
    switch (step) {

      case 0: {
        angle = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT ? -35 : 29;
        distance = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT ? 8.5 : 8;
        this.getSystems().manipulator.setIntakeSetPoint(0);
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(Drive.COUNT_FOOT);
        step++;
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          step++;
        }
        break;
      }

      case 2: {
        if (this.getSystems().drive.atAngle(angle, this.getSystems().gyro)) {
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
          lastTime = System.currentTimeMillis();
          step++;
        } else {
          if (this.getSystems().gyro.getAngle() < angle) {
            this.getSystems().drive.setLeftSetPoint(.5);
            this.getSystems().drive.setRightSetPoint(-.5);
          } else {
            this.getSystems().drive.setLeftSetPoint(-.5);
            this.getSystems().drive.setRightSetPoint(.5);
          }
        }
        break;
      }

      case 3: {
        if (this.getDeltaTime() >= 555) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
          this.getSystems().drive.resetEncoders();
          step++;
        } else {
          this.getSystems().elevator.setElevatorControlMode(ControlMode.PercentOutput);
          this.getSystems().elevator.setElevatorSetPoint(1);
        }
        break;
      }

      case 4: {
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(distance * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(distance * Drive.COUNT_FOOT);
        lastTime =  System.currentTimeMillis();
        step++;


        break;
      }

      case 5: {
        if (this.getSystems().drive.atTarget(Drive.COUNT_INCH * 4) || this.getDeltaTime() >= 3000 ) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          step++;
        }
        break;
      }

      case 6: {
        if (this.getSystems().drive.atAngle(0, this.getSystems().gyro)) {
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
          lastTime = System.currentTimeMillis();
          step++;
        } else {
          if (this.getSystems().gyro.getAngle() < (angle < 0 ? -15 : 15)) {
            this.getSystems().drive.setLeftSetPoint(.5);
            this.getSystems().drive.setRightSetPoint(-.5);
          } else {
            this.getSystems().drive.setLeftSetPoint(-.5);
            this.getSystems().drive.setRightSetPoint(.5);
          }
        }
        break;
      }

      case 7: {
        this.getSystems().manipulator.setIntakeSetPoint(-1);
        this.getSystems().manipulator.setPivotPosition(com.digitalgoats.systems.Manipulator.PivotPosition.MID);
        break;
      }

    }
  }

}
