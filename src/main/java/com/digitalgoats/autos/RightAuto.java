package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Manipulator.PivotPosition;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class RightAuto extends Auto {

  long lastTime = 0;

  public RightAuto(SystemGroup systems) {
    super("Right Auto", systems);
  }

  public long getDeltaTime() {
    return System.currentTimeMillis() - lastTime;
  }

  @Override
  public void execute() {
    if (System.currentTimeMillis() - lastTime >= 200 && this.getStep() < 2) {
      this.getSystems().drive.setTransmissionStatus(true);
    }
    this.getSystems().drive.setCruiseVelocity(9000);
    if (MatchData.getOwnedSide(GameFeature.SCALE) == OwnedSide.RIGHT) {
      ownSide(22, -85, 2);
    } else {
      driveForward(15);
    }
  }

  //region Auto Methods

  public void ownSide(double distance1, double angle1, double distance2) {
    driveForward(distance1);
    switch (this.getStep()) {

      case 3: {
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        if (this.getSystems().drive.atAngle(angle1, this.getSystems().gyro)) {
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          lastTime = System.currentTimeMillis();
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() < angle1) {
            this.getSystems().drive.setLeftSetPoint(.5);
            this.getSystems().drive.setRightSetPoint(-.5);
          } else {
            this.getSystems().drive.setLeftSetPoint(-.5);
            this.getSystems().drive.setRightSetPoint(.5);
          }
        }
        break;
      }

      case 4: {
        if (this.getSystems().elevator.getForwardSwitch()) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance2 * Drive.COUNT_FOOT);
          lastTime = System.currentTimeMillis();
          this.nextStep();
        } else {
          this.getSystems().elevator.setElevatorSetPoint(1);
        }
        break;
      }

      case 5: {
        if (this.getSystems().drive.atTarget()) {
          lastTime = System.currentTimeMillis();
          this.nextStep();
        }
        break;
      }

      case 6: {
        if (System.currentTimeMillis() - lastTime >= 500) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(-distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(-distance2 * Drive.COUNT_FOOT);

          this.nextStep();
        }
        this.getSystems().manipulator.setPivotPosition(PivotPosition.MID);
        this.getSystems().manipulator.setIntakeSetPoint(-1);
        break;
      }

      case 7: {

      }

    }
  }

  public void driveForward(double distance) {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(distance * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(distance * Drive.COUNT_FOOT);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.nextStep();
        }
        break;
      }

      case 2: {
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setLeftSetPoint(0);
        this.getSystems().drive.setRightSetPoint(0);
        this.getSystems().drive.setTransmissionStatus(false);
        this.nextStep();
        break;
      }

    }
  }

  //endregion Autos


}