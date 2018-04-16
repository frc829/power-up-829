package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  double startAngle, multiplier = 1;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      // region Setup
      case 0: {
        multiplier = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT ? 1 : -1;
        drive.resetEncoders();
        drive.lowTransmission();
        drive.coastMode();
        startAngle = gyro.getAngle();
        this.nextStep();
        break;
      } // endregion
      // region Step 1: Drive Forward
      case 1: {
        if (drive.driveDistance(12, false)) {
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 2: Turn Toward Switch
      case 2: {
        if (drive.turnAngle(gyro.getAngle(), startAngle - (multiplier * 45), .45, 10)) {
          drive.resetEncoders();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 3: Lift Up
      case 3: {
        manipulator.pivotMid();
        if (this.getDeltaTime() >= 1100) {
          elevator.stop();
          this.nextStep();
        } else {
          elevator.goTop(.5);
        }
        break;
      } // endregion
      // region Step 4: Drive
      case 4: {
        if (drive.driveDistance(multiplier == 1 ? 7 : 6, true)) {
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 5: Turn Back
      case 5: {
        if (drive.turnAngle(gyro.getAngle(), multiplier == 1 ? startAngle - 5: startAngle, .45)) {
          drive.resetEncoders();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 6: Drive at Switch
      case 6: {
        if (this.getDeltaTime() >= 100) {
          if (drive.driveDistance(multiplier == 1 ? 42 : 48, false)) {
            this.nextStep();
          }
        }
        break;
      } // endregion
      // region Step 7: Spit Cube
      case 7: {
        manipulator.pivotMid();
        if (this.getDeltaTime() >= 500) {
          manipulator.setIntakeSetPoint(0);
          drive.resetEncoders();
          this.nextStep();
        } else {
          manipulator.spitCube();
        }
        break;
      } // endregion
      // region Step 8: Back Up
      case 8: {
        if (drive.driveDistance(multiplier == 1 ? -7.5 : -7.5, true)) {
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 9: Turn to stack
      case 9: {
        if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier == 1 ? 22.5 : -22.5), .45)) {
          drive.resetEncoders();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 10: Lower lift and grab
      case 10: {
        manipulator.pivotDown();
        if (elevator.goBot(.25)) {
          drive.resetEncoders();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 11: Drive forward and suck
      case 11: {
        manipulator.openManipulator();
        manipulator.intakeIn(1);
        if (drive.driveDistance(multiplier == 1 ? 6.5 : 5.5, true)) {
          if (this.getDeltaTime() >= 1750) {
            manipulator.closeManipulator();
            drive.resetEncoders();
            this.nextStep();
          }
        }
        break;
      } // endregion
      // region Step 12: Back up
      case 12: {
        if (drive.driveDistance(multiplier == 1 ? -4 : -4, true)) {
          manipulator.intakeIn(0);
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 13: Turn back
      case 13: {
        if (drive.turnAngle(gyro.getAngle(), startAngle - (multiplier * -1.25), .45)) {
          drive.resetEncoders();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 14: Drive at switch
      case 14: {
        elevator.goUp(.625);
        manipulator.pivotMid();
        if (drive.driveDistance(74, false) && this.getDeltaTime() >= 1000) {
          elevator.stop();
          this.nextStep();
        }
        break;
      } // endregion
      // region Step 15: Spit
      case 15: {
        if (this.getDeltaTime() >= 1000) {
          manipulator.intakeIn(0);
          drive.resetEncoders();
          this.nextStep();
        }
        manipulator.spitCube();
        break;
      } // endregion
      // region Step 16: Back up
      case 16: {
        drive.driveDistance(-4, true);
        break;
      } // endregion
    }
  }

}
