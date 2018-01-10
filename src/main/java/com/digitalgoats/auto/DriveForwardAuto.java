package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;

public class DriveForwardAuto extends Auto {

  public DriveForwardAuto(SystemsGroup systemsGroup) {
    super("Drive Forward", systemsGroup);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        this.setStartTime(System.currentTimeMillis());
        this.nextStep();
        break;
      }

      case 1: {
        if (this.getDeltaTime() > 3000) { this.nextStep(); }
        this.getSystemsGroup().drive.driveStraightNavX(1);
        break;
      }

      case 2: {
        this.getSystemsGroup().drive.setDriveSpeed(0, 0);
        break;
      }

    }
  }

}
