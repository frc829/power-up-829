package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;

public class DriveForwardAuto extends Auto {

  public DriveForwardAuto(SystemGroup systems) {
    super("Drive Forward", systems);
  }

  @Override
  public void execute() {
    System.out.println(this.getStep());
    switch (this.getStep()) {

      case 0: {
        drive.resetEncoders();
        drive.highTransmission();
        drive.brakeMode();
        drive.driveDistance(11, true);
        this.nextStep();
        break;
      }

      case 1: {
        if (drive.driveDistance(11, true)) {
          this.nextStep();
        }
        break;
      }

    }
  }

}
