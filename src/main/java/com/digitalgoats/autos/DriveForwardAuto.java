package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;

public class DriveForwardAuto extends Auto {

  public DriveForwardAuto(SystemGroup systems) {
    super("Drive Forward", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setTransmissionStatus(false);
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(7.5 * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(7.5 * Drive.COUNT_FOOT);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        }
        break;
      }

    }
  }

}
