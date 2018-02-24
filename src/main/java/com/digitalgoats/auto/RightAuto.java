package com.digitalgoats.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Manipulator;
import com.digitalgoats.systems.SystemsGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class RightAuto extends Auto {

  private OwnedSide switchSide, scaleSide;

  public RightAuto(SystemsGroup systemsGroup) {
    super("Right Auto", systemsGroup);
  }

  @Override
  public void execute() {
    switchSide = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
    scaleSide = MatchData.getOwnedSide(GameFeature.SCALE);
    if (switchSide == OwnedSide.LEFT && scaleSide == switchSide) {
      this.bothOn();
    } else if (switchSide == OwnedSide.LEFT) {
      this.oneSide(12.125, .5,750);
    } else if (scaleSide == OwnedSide.LEFT) {
      this.oneSide(30, 2,2500);
    } else {
      this.noneOn();
    }
  }

  public void bothOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

  public void oneSide(double driveDist, double driveDistB, long liftTime) {
    switch (this.getStep()) {

      case 0: {
        this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_UP);
        this.systemsGroup.manipulator.setGripSolenoidStatus(true);
        this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
        this.systemsGroup.drive.setDriveSpeed(driveDist * Drive.FOOT_COUNT_L, driveDist * Drive.FOOT_COUNT_R);
        this.setStartTime(System.currentTimeMillis());
        this.nextStep();
        break;
      }

      case 1: {
        if (this.systemsGroup.drive.atTarget() || this.getDeltaTime() >= 5000) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.systemsGroup.drive.setDriveSpeed(0, 0);
          this.nextStep();
        }
        this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_MID);
        break;
      }

      case 2: {
        if (this.atAngle(-90)) {
          this.systemsGroup.drive.setDriveSpeed(0, 0);
          this.setStartTime(System.currentTimeMillis());
          this.nextStep();
        } else {
          if (this.systemsGroup.navx.getAngle() < -90) {
            this.systemsGroup.drive.setDriveSpeed(.5, -.5);
          } else if (this.systemsGroup.navx.getAngle() > -90) {
            this.systemsGroup.drive.setDriveSpeed(-.5, .5);
          }
        }
        break;
      }

      case 3: {
        if (this.getDeltaTime() >= liftTime) {
          this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_MID);
          this.systemsGroup.arm.setStageSpeed(.0625);
          this.systemsGroup.drive.resetSensors();
          this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
          this.systemsGroup.drive.setLeftSpeed(driveDistB * Drive.FOOT_COUNT_L);
          this.systemsGroup.drive.setRightSpeed(driveDistB * Drive.FOOT_COUNT_R);
          this.nextStep();
        } else {
          this.systemsGroup.arm.setStageSpeed(.75);
        }
        break;
      }

      case 4: {
        if (this.systemsGroup.drive.atTarget()) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.systemsGroup.drive.setDriveSpeed(0, 0);
          this.setStartTime(System.currentTimeMillis());
          this.nextStep();
        }
        break;
      }

      case 5: {
        if (this.getDeltaTime() >= 1000) {
          this.systemsGroup.manipulator.setWheelSpeed(0);
          this.nextStep();
        } else {
          this.systemsGroup.manipulator.setGripSolenoidStatus(false);
          this.systemsGroup.manipulator.setWheelSpeed(-.75);
        }
        break;
      }

    }
  }

  public void noneOn() {
    switch (this.getStep()) {

      case 0: {
        break;
      }

    }
  }

}