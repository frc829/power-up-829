package com.digitalgoats.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.systems.Arm;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Manipulator;
import com.digitalgoats.systems.SystemsGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  public OwnedSide side = null;
  double targetAngle = 0;

  public MiddleAuto(SystemsGroup systemsGroup) {
    super("Middle Auto", systemsGroup);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        side = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
        this.systemsGroup.navx.resetDisplacement();
        this.systemsGroup.manipulator.setGripSolenoidStatus(true);
        this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
        this.systemsGroup.drive.setDriveSpeed(15 * Drive.INCH_COUNT, 15 * Drive.INCH_COUNT);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.systemsGroup.drive.atTarget()) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.systemsGroup.drive.setDriveSpeed(0, 0);
          this.nextStep();
        }
        break;
      }

      case 2: {
        if (side == OwnedSide.LEFT) {
          targetAngle = -45;
        } else {
          targetAngle = 45;
        }
        this.nextStep();
        break;
      }

      case 3: {
        if (this.systemsGroup.drive.atAngle(targetAngle)) {
          this.nextStep();
        } else {
          if (this.systemsGroup.navx.getAngle() < targetAngle) {
            this.systemsGroup.drive.setDriveSpeed(.25, -.25);
          } else {
            this.systemsGroup.drive.setDriveSpeed(-.25, .25);
          }
        }
        break;
      }

      case 4: {
        this.systemsGroup.drive.resetSensors();
        this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
        this.systemsGroup.drive.setDriveSpeed(3 * Drive.FOOT_COUNT, 3 * Drive.FOOT_COUNT);
        this.nextStep();
        break;
      }

      case 5: {
        if (this.systemsGroup.drive.atTarget()) {
          this.nextStep();
        }
        break;
      }

      case 6: {
        this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
        this.systemsGroup.drive.setDriveSpeed(0, 0);
        this.setStartTime(System.currentTimeMillis());
        this.nextStep();
        break;
      }

      case 7: {
        if (this.getDeltaTime() >= 2500) {
          this.nextStep();
        } else {
          this.systemsGroup.arm.setStageSpeed(.375);
        }
        break;
      }

      case 8: {
        this.systemsGroup.arm.setStageSpeed(.0625);
        this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
        this.systemsGroup.drive.setDriveSpeed(0, 0);
        this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_MID);
        this.setStartTime(System.currentTimeMillis());
        this.nextStep();
        break;
      }

      case 9: {
        if (this.getDeltaTime() >= 1000) {
          this.setStartTime(System.currentTimeMillis());
          this.nextStep();
        }
        break;
      }

      case 10: {
        if (this.getDeltaTime() >= 3000) {
          this.nextStep();
        } else {
          this.systemsGroup.manipulator.setGripSolenoidStatus(false);
          this.systemsGroup.manipulator.setWheelSpeed(1);
        }
        break;
      }

      case 11: {
        this.systemsGroup.manipulator.setGripSolenoidStatus(false);
        this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_LOW);
        this.systemsGroup.manipulator.setWheelSpeed(0);
        break;
      }

    }
  }

}
