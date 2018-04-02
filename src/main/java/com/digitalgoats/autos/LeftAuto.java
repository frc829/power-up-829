package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;

public class LeftAuto extends Auto {

  double startAngle = 0;

  public LeftAuto(SystemGroup systems) {
    super("Left Auto", systems);
  }

  @Override
  public void execute() {
    System.out.println(this.getStep());
    switch (this.getStep()) {

      case 0: {
        startAngle = gyro.getAngle();
        drive.resetEncoders();
        drive.highTransmission();
        drive.brakeMode();
        drive.driveDistance(305, false);
        this.nextStep();
        break;
      }

      case 1: {
        if (drive.driveDistance(305, false)) {
          this.nextStep();
        }
        break;
      }

      case 2: {
        if (this.getDeltaTime() >= 500) {
          startAngle = gyro.getAngle();
          this.nextStep();
        }
        break;
      }

      case 3: {
        if (drive.turnAngle(gyro.getAngle(), startAngle + 90, .45, 8.75)) {
          this.nextStep();
        }
        break;
      }

    }
  }

}
