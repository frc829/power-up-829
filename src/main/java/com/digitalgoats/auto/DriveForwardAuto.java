package com.digitalgoats.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.systems.SystemsGroup;

/**
 * Simple Drive Forward Auto
 * @author Blake
 */
public class DriveForwardAuto extends Auto {

  private final double COUNTS_PER_INCH = 45.28301886792453;

  public DriveForwardAuto(SystemsGroup systemsGroup) {
    super("Drive Forward", systemsGroup);
  }

  @Override
  public void execute() {

    switch (this.getStep()) {

      // Set starting time and go to next step
      case 0: {
        this.setStartTime(System.currentTimeMillis());
        this.systemsGroup.navx.resetDisplacement();
        this.systemsGroup.drive.setTransmissionStatus(false);
        this.systemsGroup.drive.resetSensors();
        this.nextStep();
        break;
      }

      /*
       * If 3 seconds have passed go to next step
       * Otherwise drive straight
       */
      case 1: {
        this.systemsGroup.drive.setControlMode(ControlMode.MotionMagic);
        this.systemsGroup.drive.setDriveSpeed(9 * 12 * COUNTS_PER_INCH, 9 * 12 * COUNTS_PER_INCH);
        this.nextStep();
        break;
      }

      // Turn
      case 2: {
        break;
      }

    }
  }

}
