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

  public MiddleAuto(SystemsGroup systemsGroup) {
    super("Middle Auto", systemsGroup);
  }

  @Override
  public void execute() {
    side = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
    runAuto(
      side == OwnedSide.LEFT ? -45 : 45,
      side == OwnedSide.LEFT ? Drive.FOOT_COUNT : Drive.FOOT_COUNT,
      side == OwnedSide.LEFT ? 10 * Drive.FOOT_COUNT : 7 * Drive.FOOT_COUNT,
      side == OwnedSide.LEFT ? 2.75 * Drive.FOOT_COUNT : 5.75 * Drive.FOOT_COUNT
    );
  }

  public void runAuto(double angle, double distanceA, double distanceB, double distanceC) {
    System.out.println(this.getStep());
    switch (this.getStep()) {

      case 0: {
        this.systemsGroup.manipulator.setGripSolenoidStatus(true);
        this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
        this.systemsGroup.drive.setDriveSpeed(distanceA, distanceA);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.systemsGroup.drive.atTarget()) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.nextStep();
        }
        break;
      }

      case 2: {
        if (this.atAngle(angle)) {
            this.systemsGroup.drive.setDriveSpeed(0, 0);
            this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
            this.systemsGroup.drive.setLeftSpeed(distanceB);
            this.systemsGroup.drive.setRightSpeed(distanceB);
            this.systemsGroup.drive.resetSensors();
            this.nextStep();
        } else if (this.systemsGroup.navx.getAngle() < angle) {
            this.systemsGroup.drive.setDriveSpeed(.25, -.25);
        } else {
            this.systemsGroup.drive.setDriveSpeed(-.25, .25);
        }
        break;
      }

      case 3: {
        if (this.systemsGroup.drive.atTarget()) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.nextStep();
        }
        break;
      }

      case 4: {
        if (this.atAngle(0)) {
            this.setStartTime(System.currentTimeMillis());
            this.systemsGroup.drive.setDriveSpeed(0, 0);
            this.nextStep();
        } else if (this.systemsGroup.navx.getAngle() < 0) {
            this.systemsGroup.drive.setDriveSpeed(.25, -.25);
        } else {
            this.systemsGroup.drive.setDriveSpeed(-.25, .25);
        }
        break;
      }

      case 5: {
        if (this.getDeltaTime() >= 1500) {
          this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
          this.systemsGroup.drive.resetSensors();
          this.systemsGroup.arm.setStageSpeed(.0625);
          this.systemsGroup.drive.setDriveSpeed(distanceC, distanceC);
          this.nextStep();
        } else {
          this.systemsGroup.arm.setStageSpeed(.5);
          this.systemsGroup.manipulator.setPivotSolenoidStatus(Manipulator.PIVOT_MID);
        }
        break;
      }

      case 6: {
        if (this.systemsGroup.drive.atTarget()) {
          this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
          this.systemsGroup.drive.setDriveSpeed(0, 0);
          this.nextStep();
        }
        break;
      }

      case 7: {
        this.systemsGroup.manipulator.setWheelSpeed(1);
        break;
      }

    }
  }

}
