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

    // Go for owned side of scale
    if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == side) {
      switch (this.getStep()) {
        // Setup
        case 0: {
          drive.resetEncoders();
          drive.changePeaks(1);
          drive.highTransmission();
          drive.brakeMode();
          manipulator.closeManipulator();
          this.nextStep();
          break;
        }
        // Drive to edge of null zone
        case 1: {
          if (drive.driveDistance(274, false)) {
            startAngle = gyro.getAngle();
            this.nextStep();
          }
          break;
        }
        // Lift elevator to top and pivot mid
        case 2: {
          manipulator.pivotMid();
          if (elevator.goTop()) {
            this.nextStep();
          }
          break;
        }
        // Turn 55 towards scale
        case 3: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 55), .45, 15)) {
            this.nextStep();
          }
          break;
        }
        // Spit cube
        case 4: {
          manipulator.spitCube();
          if (this.getDeltaTime() >= 1000) {
            manipulator.setIntakeSetPoint(0);
            manipulator.openManipulator();
            this.nextStep();
          }
          break;
        }
        // Drop elevator to bottom and pivot down
        case 5: {
          manipulator.pivotDown();
          if (elevator.goDown()) {
            this.nextStep();
          }
          break;
        }
        // Turn 90 and open manipulator
        case 6: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 142.5), .45, 10)) {
            drive.resetEncoders();
            drive.changePeaks(.5);
            this.nextStep();
          }
          break;
        }
        // Drive forward and grab cube
        case 7: {
          manipulator.openManipulator();
          manipulator.intakeIn(1);
          System.out.println(this.getDeltaTime());
          if (drive.driveDistance(7.5, true) && this.getDeltaTime() >= 3000) {
            manipulator.closeManipulator();
            manipulator.setIntakeSetPoint(0);
            drive.resetEncoders();
            drive.changePeaks(1);
            this.nextStep();
          }
          break;
        }
        // Drive backward
        case 8: {
          if (drive.driveDistance(-5.5, true)) {
            this.nextStep();
          }
          break;
        }
        // Turn -90
        case 9: {
          if (drive.turnAngle(gyro.getAngle(), startAngle + (multiplier * 45), .45, 10)) {
            this.nextStep();
          }
          break;
        }
        // Lift elevator to top and pivot mid
        case 10: {
          manipulator.pivotMid();
          if (elevator.goTop()) {
            this.nextStep();
          }
          break;
        }
        // Spit cube
        case 11: {
          manipulator.spitCube();
          if (this.getDeltaTime() >= 1000) {
            manipulator.setIntakeSetPoint(0);
            this.nextStep();
          }
          break;
        }
      }
    }
    // TODO Scale on other side
    else {
    }

  }

}
