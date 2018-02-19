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
    switch (this.getCurrentStep()) {

      case 0: {
        this.getSystems().drive.setControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.resetEncoders();
        this.nextStep();
        break;
      }

      case 1: {
        this.getSystems().drive.setLeftSetPoint(Drive.FOOT_VALUE * 9);
        this.getSystems().drive.setRightSetPoint(Drive.FOOT_VALUE * 9);
        if (this.getSystems().drive.getLeftPosition() >= Drive.FOOT_VALUE * 9) {
          if (this.getSystems().drive.getRightPosition() >= Drive.FOOT_VALUE * 9) {
            this.nextStep();
          }
        }
        break;
      }

      case 2: {
        this.getSystems().drive.setControlMode(ControlMode.Disabled);
        this.getSystems().drive.setRightSetPoint(0);
        this.getSystems().drive.setLeftSetPoint(0);
        this.getSystems().drive.resetEncoders();
        this.nextStep();
        break;
      }

    }
  }

}
