package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;

/**
 * Simple Drive Forward Auto
 * @author Blake
 */
public class DriveForwardAuto extends Auto {

  public DriveForwardAuto(SystemsGroup systemsGroup) {
    super("Drive Forward", systemsGroup);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      // Set starting time and go to next step
      case 0: {
        this.setStartTime(System.currentTimeMillis());
        this.nextStep();
        break;
      }

      /*
       * If 3 seconds have passed go to next step
       * Otherwise drive straight
       */
      case 1: {
        if (this.getDeltaTime() > 3000) { this.nextStep(); }
        this.getSystemsGroup().drive.driveStraightNavX(1);
        break;
      }

      // Stop drive motors
      case 2: {
        this.getSystemsGroup().drive.setDriveSpeed(0, 0);
        break;
      }

    }
  }

}
