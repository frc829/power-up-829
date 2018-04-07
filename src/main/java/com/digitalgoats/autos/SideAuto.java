package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class SideAuto extends Auto {

  double startAngle, multiplier;
  OwnedSide side;

  public SideAuto(String name, SystemGroup systemGroup, OwnedSide side) {
    super(name, systemGroup);
    this.side = side;
    this.multiplier = side == OwnedSide.LEFT ? 1 : -1;
  }

  @Override
  public void execute() {

    // region Same Side
    if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == side) {
      switch (this.getStep()) {
        // region Setup
        case 0: {
          drive.resetEncoders();
          drive.changePeaks(1);
          drive.highTransmission();
          drive.brakeMode();
          manipulator.closeManipulator();
          this.nextStep();
          break;
        } // endregion
        // region Step 1: Drive to edge of null zone
        case 1: {
          if (drive.driveDistance(294, false)) {
            startAngle = gyro.getAngle();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 2: Lift elevator to top and pivot mid
        case 2: {
          manipulator.pivotMid();
          if (elevator.goTop()) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 3: Turn 55 towards scale
        case 3: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 55), .625, 7.5)) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 4: Spit cube
        case 4: {
          manipulator.spitCube();
          if (this.getDeltaTime() >= 1000) {
            manipulator.setIntakeSetPoint(0);
            manipulator.openManipulator();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 5: Drop elevator to bottom and pivot down
        case 5: {
          manipulator.pivotDown();
          if (elevator.goBot() || this.getDeltaTime() >= 2000) {
            elevator.stop();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 6: Turn 90 and open manipulator
        case 6: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 130), .625, 7.5)) {
            drive.resetEncoders();
            drive.changePeaks(.5);
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 7: Drive forward and grab cube
        case 7: {
          manipulator.openManipulator();
          manipulator.intakeIn(1);
          System.out.println(this.getDeltaTime());
          if (drive.driveDistance(9, true) && this.getDeltaTime() >= 3000) {
            manipulator.closeManipulator();
            manipulator.setIntakeSetPoint(0);
            drive.resetEncoders();
            drive.changePeaks(1);
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 8: Drive backward
        case 8: {
          if (drive.driveDistance(-3, true)) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 9: Turn -90
        case 9: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 67.5), .625, 15)) {
            drive.resetEncoders();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 10: Drive forward 5 feet
        case 10: {
          if (drive.driveDistance(14, false)) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 11: Lift elevator to top and pivot mid
        case 11: {
          manipulator.pivotMid();
          if (elevator.goTop()) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 12: Spit cube
        case 12: {
          manipulator.spitCube();
          if (this.getDeltaTime() >= 1000) {
            manipulator.setIntakeSetPoint(0);
            this.nextStep();
          }
          break;
        } // endregion
      }
    } // endregion
    // region Other Side
    else {
      switch (this.getStep()) {
        // region Setup
        case 0: {
          drive.resetEncoders();
          drive.changePeaks(1);
          drive.highTransmission();
          drive.brakeMode();
          manipulator.closeManipulator();
          manipulator.pivotUp();
          this.nextStep();
          break;
        } // endregion
        // region Step 1: Drive forward
        case 1: {
          if (drive.driveDistance(240, false)) {
            startAngle = gyro.getAngle();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 2: Turn Right 90
        case 2: {
          if (this.getDeltaTime() >= 500) {
            if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 90), .35, 7)) {
              drive.resetEncoders();
              this.nextStep();
            }
          }
          break;
        } // endregion
        // region Step 3: Drive Forward
        case 3: {
          if (this.getDeltaTime() >= 500) {
            if (drive.driveDistance(208, false)) {
              startAngle = gyro.getAngle();
              drive.drive(0);
              //this.nextStep();
            }
          }
          break;
        } // endregion
        //region Step 4: Turn Left 90
        case 4: {
          if (this.getDeltaTime() >= 500) {
            if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * -90), .45, 10)) {
              this.nextStep();
            }
          }
          break;
        } // endregion
        // region Step 5: Lift up and pivot mid
        case 5: {
          manipulator.pivotMid();
          if (elevator.goTop(.75) || this.getDeltaTime() >= 2000) {
            drive.resetEncoders();
            drive.lowTransmission();
            drive.changePeaks(.5);
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 6: Drive forward
        case 6: {
          if (this.getDeltaTime() >= 250) {
            if (drive.driveDistance(36, false)) {
              this.nextStep();
            }
          }
          break;
        } // endregion
        // region Step 7: Spit cube
        case 7: {
          manipulator.spitCube(.5);
          if (this.getDeltaTime() >= 1000) {
            manipulator.setIntakeSetPoint(0);
            drive.resetEncoders();
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 8: Back up
        case 8: {
          if (drive.driveDistance(-48, false)) {
            this.nextStep();
          }
          break;
        } // endregion
        // region Step 9: Bring lift down and pivot down
        case 9: {
          manipulator.pivotDown();
          if (elevator.goBot(.75)) {
            this.nextStep();
          }
          break;
        } // endregion
      }
    }

  }

}
