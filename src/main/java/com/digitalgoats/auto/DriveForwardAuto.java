package com.digitalgoats.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.systems.SystemsGroup;

/**
 * Simple Drive Forward Auto
 * @author Blake
 */
public class DriveForwardAuto extends Auto {

  private final double UNITS_PER_INCH = -0.0209446359187999;

  public DriveForwardAuto(SystemsGroup systemsGroup) {
    super("Drive Forward", systemsGroup);
  }

  @Override
  public void execute() {

    System.out.println(12 * UNITS_PER_INCH);
    switch (this.getStep()) {

      // Set starting time and go to next step
      case 0: {
        this.setStartTime(System.currentTimeMillis());
        this.systemsGroup.navx.resetDisplacement();
        this.systemsGroup.drive.setTransmissionStatus(false);
        this.nextStep();
        break;
      }

      /*
       * If 3 seconds have passed go to next step
       * Otherwise drive straight
       */
      case 1: {
        if (this.systemsGroup.navx.getDisplacementX() <= 12 * UNITS_PER_INCH) { this.nextStep(); }
        this.systemsGroup.drive.setControlMode(ControlMode.Velocity);
        this.systemsGroup.drive.setDriveSpeed(360, 360);
        break;
      }

      // Stop drive motors
      case 2: {
        this.systemsGroup.drive.setControlMode(ControlMode.PercentOutput);
        this.getSystemsGroup().drive.setDriveSpeed(0, 0);
        break;
      }

    }
  }

}
