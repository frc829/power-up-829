package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class SideNoCrossAuto extends Auto {

  double startAngle, multiplier;
  boolean stepActive;
  OwnedSide side;

  public SideNoCrossAuto(String name, SystemGroup systemGroup, OwnedSide side) {
    super(name, systemGroup);
    this.side = side;
    this.multiplier = side == OwnedSide.LEFT ? 1 : -1;
  }

  @Override
  public void execute() {

    // region Same Side
    if (MatchData.getOwnedSide(GameFeature.SCALE) == side) {
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
          if (drive.driveDistance(-2, true) || this.getDeltaTime() >= 5000) {
            if (elevator.goBot() || this.getDeltaTime() >= 7000) {
              elevator.stop();
              this.nextStep();
            }
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
            //this.nextStep();
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
      // region Own Switch
      if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == side) {
        switch (this.getStep()) {
          // region Setup
          case 0: {
            drive.resetEncoders();
            drive.highTransmission();
            drive.brakeMode();
            this.nextStep();
            break;
          } // endregion
          // region Step 1: Wait
          case 1: {
            if (this.getDeltaTime() >= 500) {
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 2: Drive toward switch
          case 2: {
            if (drive.driveDistance(12, true)) {
              startAngle = gyro.getAngle();
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 3: Turn 90
          case 3: {
            if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 85), .45, 7.5)) {
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 4: Lift Elevator
          case 4: {
            manipulator.pivotMid();
            if (this.getDeltaTime() >= 750) {
              elevator.stop();
              drive.resetEncoders();
              this.nextStep();
            } else {
              elevator.goUp(.5);
            }
            break;
          } // endregion
          // region Step 5: Go forward
          case 5: {
            if (drive.driveDistance(30, false)) {
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 6: Spit
          case 6: {
            manipulator.spitCube();
            if (this.getDeltaTime() >= 500) {
              manipulator.intakeIn(0);
              drive.resetEncoders();
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 7: Back up
          case 7: {
            if (drive.driveDistance(-3, true)) {
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 8: Turn back 90
          case 8: {
            if (drive.turnAngle(gyro.getAngle(), startAngle, .45)) {
              drive.resetEncoders();
              this.nextStep();
            }
            break;
          } // endregion
          // region Step 9: Drive forward
          case 9: {
            if (drive.driveDistance(4, true)) {
              this.nextStep();
            }
            break;
          } // endregion
        }
      } // endregion
      // region No Switch
      else {
        switch (this.getStep()) {
          // region Setup
          case 0: {
            drive.highTransmission();
            drive.brakeMode();
            drive.resetEncoders();
            this.nextStep();
            break;
          } // endregion
          // region Step 1: Wait for shift
          case 1: {
            drive.driveDistance(240, false);
            break;
          } // endregion
        }
      } // endregion
    }

  }

}
