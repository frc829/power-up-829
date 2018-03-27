package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Elevator;
import com.digitalgoats.systems.Manipulator.PivotPosition;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    switch (step) {

      // Set drive to go forward a foot
      case 0: {
        angle = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT ? -35 : 29;
        distance = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT ? 8 : 8;
        this.getSystems().manipulator.setIntakeSetPoint(0);
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setTransmissionStatus(true);
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(Drive.COUNT_FOOT);
        step++;
        break;
      }

      // Go to next step once you've driven a foot
      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          step++;
        }
        break;
      }

      // Turn to angle for owned side
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

      // Lift elevator for set time
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

      // Tell robot to drive selected distance
      case 4: {
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(distance * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(distance * Drive.COUNT_FOOT);
        lastTime =  System.currentTimeMillis();
        step++;
        break;
      }

      // Go to next step when within 2 inches or if 3 seconds have passed
      case 5: {
        if (this.getSystems().drive.atTarget(Drive.COUNT_INCH * 2) || this.getDeltaTime() >= 3500) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          step++;
        }
        break;
      }

      // Drive back to angle 0
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

      // Spit and stop grip while driving forward
      case 7: {
        if (System.currentTimeMillis() - lastTime >= 2000) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
          step++;
        } else if (System.currentTimeMillis() - lastTime >= 1000) {
          this.getSystems().drive.setLeftSetPoint(-.25);
          this.getSystems().drive.setRightSetPoint(-.25);
        } else if (System.currentTimeMillis() - lastTime >= 500) {
          this.getSystems().manipulator.setIntakeSetPoint(-1);
          this.getSystems().manipulator.setGripStatus(false);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.MID);
        } else {
          this.getSystems().drive.setLeftSetPoint(.5);
          this.getSystems().drive.setRightSetPoint(.5);
        }
        break;
      }

      // Drop elevator
      case 8: {
        if (this.getSystems().elevator.getReverseSwitch()) {
          this.getSystems().elevator.setElevatorSetPoint(0);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(-5 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(-5 * Drive.COUNT_FOOT);
          step++;
        } else {
          this.getSystems().elevator.setElevatorSetPoint(-.75);
        }
        break;
      }

      case 9: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          angle = angle < 0 ? 25 : -25;
          step++;
        }
        break;
      }

      case 10: {
        if (this.getSystems().drive.atAngle(angle, this.getSystems().gyro, 5)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.changePeaks(.75);
          this.getSystems().drive.setLeftSetPoint(5.75 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(5.75 * Drive.COUNT_FOOT);
          lastTime = System.currentTimeMillis();
          step++;
        } else {
          if (this.getSystems().gyro.getAngle() < angle) {
            this.getSystems().drive.setLeftSetPoint(.45);
            this.getSystems().drive.setRightSetPoint(-.45);
          } else {
            this.getSystems().drive.setLeftSetPoint(-.45);
            this.getSystems().drive.setRightSetPoint(.45);
          }
        }
        break;
      }

      case 11: {
        this.getSystems().manipulator.setIntakeSetPoint(1);
        this.getSystems().manipulator.setGripStatus(false);
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.getSystems().manipulator.setGripStatus(true);
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setLeftSetPoint(-Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(-Drive.COUNT_FOOT);
          step++;
        }
        break;
      }

      case 12: {
        break;
      }

    }
  }

}
