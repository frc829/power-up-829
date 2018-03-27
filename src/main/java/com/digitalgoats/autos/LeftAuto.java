package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Manipulator.PivotPosition;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import javax.naming.ldap.Control;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class LeftAuto extends Auto {

  long lastTime = 0;
  double turnSpeed = .45;

  public LeftAuto(SystemGroup systems) {
    super("Left Auto", systems);
    this.getSystems().drive.setTransmissionStatus(false);
  }

  public long getDeltaTime() {
    return System.currentTimeMillis() - lastTime;
  }

  @Override
  public void execute() {
    SmartDashboard.putNumber("Intake Current", this.getSystems().manipulator.getIntakeCurrent());
    this.getSystems().drive.setCruiseVelocity(9000);
    if (MatchData.getOwnedSide(GameFeature.SCALE) == OwnedSide.LEFT) {
      ownSide(16.9, 50, 0, true);
    } else if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT) {
      ownSide(10, 90, 1.2, false);
      copCube(-1, 15, 5.125, 85, 4.625, 170, .25);
    } else {
      driveForward(15);
      //noneOwned(16, -65, 15.125, -15, .25);
    }
  }

  //region Auto Methods

  public void ownSide(double distance1, double angle1, double distance2, boolean allUp) {
    driveForward(distance1);
    switch (this.getStep()) {

      case 3: {
        this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        if (this.getSystems().drive.atAngle(angle1, this.getSystems().gyro)) {
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          lastTime = System.currentTimeMillis();
          this.getSystems().drive.setTransmissionStatus(false);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() < angle1) {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          }
        }
        break;
      }

      case 4: {
        if ((allUp && this.getSystems().elevator.getForwardSwitch()) ||
            (!allUp && System.currentTimeMillis() - lastTime >= 500)) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
          lastTime = System.currentTimeMillis();
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setCruiseVelocity(3000);
          this.getSystems().drive.setLeftSetPoint(distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance2 * Drive.COUNT_FOOT);
          this.nextStep();
        } else {
          this.getSystems().elevator.setElevatorSetPoint(1);
        }
        break;
      }

      case 5: {
        if (this.getSystems().drive.atTarget()) {
          lastTime = System.currentTimeMillis();
          this.nextStep();
        }
        break;
      }

      case 6: {
        if (System.currentTimeMillis() - lastTime >= 1000) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
          this.getSystems().drive.resetEncoders();
          this.nextStep();
        }
        this.getSystems().manipulator.setPivotPosition(PivotPosition.MID);
        this.getSystems().manipulator.setIntakeSetPoint(-.75);
        break;
      }

    }
  }

  public void driveForward(double distance) {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(distance * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(distance * Drive.COUNT_FOOT);
        this.nextStep();
        break;
      }

      case 1: {
        if (this.getSystems().drive.atTarget()) {
          this.nextStep();
        }
        break;
      }

      case 2: {
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setLeftSetPoint(0);
        this.getSystems().drive.setRightSetPoint(0);
        this.nextStep();
        break;
      }

    }
  }

  public void copCube(double backup, double angle1, double distance1, double angle2, double distance2, double angle3, double distance3) {
    switch (this.getStep()) {

      case 7: {
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.setLeftSetPoint(backup * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(backup * Drive.COUNT_FOOT);
        this.getSystems().manipulator.setIntakeSetPoint(0);
        if (this.getSystems().drive.atTarget()) {
          this.nextStep();
        }
        break;
      }

      case 8: {
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().manipulator.setIntakeSetPoint(0);
        if (this.getSystems().drive.atAngle(angle1, this.getSystems().gyro)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(distance1 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance1 * Drive.COUNT_FOOT);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() < angle1) {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          }
        }
        break;
      }

      case 9: {
        this.getSystems().drive.setTransmissionStatus(true);
        this.getSystems().manipulator.setIntakeSetPoint(-1);
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        }
        if (!this.getSystems().elevator.getReverseSwitch()) {
          this.getSystems().elevator.setElevatorSetPoint(-.5);
          this.getSystems().manipulator.setPivotPosition(PivotPosition.DOWN);
        } else {
          this.getSystems().elevator.setElevatorSetPoint(0);
        }
        break;
      }

      case 10: {
        if (this.getSystems().drive.atAngle(angle2, this.getSystems().gyro)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.changePeaks(.75);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() < angle2) {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          }
        }
        break;
      }

      case 11: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          lastTime = System.currentTimeMillis();
          this.nextStep();
        }
        break;
      }

      case 12: {
        if (this.getSystems().drive.atAngle(angle3, this.getSystems().gyro)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(distance3 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance3 * Drive.COUNT_FOOT);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() < angle3) {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          }
        }
        break;
      }

      case 13: {
        if (this.getSystems().drive.atTarget() || this.getSystems().manipulator.getIntakeCurrent() >= 30) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          lastTime = System.currentTimeMillis();
          this.nextStep();
        }
        break;
      }

      case 14: {
        if (System.currentTimeMillis() - lastTime >= 1000 || this.getSystems().manipulator.getIntakeCurrent() >= 30) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.getSystems().manipulator.setGripStatus(true);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setLeftSetPoint(-Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(-Drive.COUNT_FOOT);
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.nextStep();
        }
        break;
      }

      case 15: {
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

  //endregion Autos

  public void noneOwned(double distance1, double angle1, double distance2, double angle2,
      double distance3) {
    switch (this.getStep()) {

      case 0: {
        this.getSystems().drive.changePeaks(.75);
        this.getSystems().drive.setTransmissionStatus(false);
        this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
        this.getSystems().drive.resetEncoders();
        this.getSystems().drive.setLeftSetPoint(distance1 * Drive.COUNT_FOOT);
        this.getSystems().drive.setRightSetPoint(distance1 * Drive.COUNT_FOOT);
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

      case 2: {
        if (this.getSystems().drive.atAngle(angle1, this.getSystems().gyro)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(distance2 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance2 * Drive.COUNT_FOOT);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() > angle1) {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          }
        }
        break;
      }

      case 3: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        }
        break;
      }

      case 4: {
        if (this.getSystems().drive.atAngle(angle2, this.getSystems().gyro)) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(distance3 * Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(distance3 * Drive.COUNT_FOOT);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() > angle2) {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          }
        }
        break;
      }

      case 5: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        }
        break;
      }

      case 6: {
        if (this.getSystems().elevator.getForwardSwitch()) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
          lastTime = System.currentTimeMillis();
          this.nextStep();
        } else {
          this.getSystems().manipulator.setPivotPosition(PivotPosition.MID);
          this.getSystems().elevator.setElevatorSetPoint(.625);
        }
        break;
      }

      case 7: {
        if (System.currentTimeMillis() - lastTime >= 1000) {
          this.getSystems().manipulator.setIntakeSetPoint(0);
          this.nextStep();
        }
        this.getSystems().manipulator.setIntakeSetPoint(-1);
        break;
      }

      case 8: {
        this.getSystems().manipulator.setIntakeSetPoint(0);
        if (this.getSystems().elevator.getReverseSwitch()) {
          this.getSystems().elevator.setElevatorSetPoint(0);
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.nextStep();
        } else {
          this.getSystems().elevator.setElevatorSetPoint(-1);
        }
        break;
      }

      case 9: {
        if (this.getSystems().drive.atAngle(-180, this.getSystems().gyro)) {
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.MotionMagic);
          this.getSystems().drive.setLeftSetPoint(Drive.COUNT_FOOT);
          this.getSystems().drive.setRightSetPoint(Drive.COUNT_FOOT);
          this.getSystems().manipulator.setIntakeSetPoint(1);
          this.getSystems().manipulator.setGripStatus(false);
          this.nextStep();
        } else {
          if (this.getSystems().gyro.getAngle() > -180) {
            this.getSystems().drive.setLeftSetPoint(-turnSpeed);
            this.getSystems().drive.setRightSetPoint(turnSpeed);
          } else {
            this.getSystems().drive.setLeftSetPoint(turnSpeed);
            this.getSystems().drive.setRightSetPoint(-turnSpeed);
          }
        }
        break;
      }

      case 10: {
        if (this.getSystems().drive.atTarget()) {
          this.getSystems().drive.resetEncoders();
          this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
          this.getSystems().drive.setLeftSetPoint(0);
          this.getSystems().drive.setRightSetPoint(0);
          lastTime = System.currentTimeMillis();
        }
        break;
      }

      case 11: {
        this.getSystems().manipulator.setGripStatus(true);
        this.getSystems().manipulator.setIntakeSetPoint(0);
        if (System.currentTimeMillis() - lastTime >= 555) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
          this.nextStep();
        } else {
          this.getSystems().elevator.setElevatorSetPoint(.5);
        }
        break;
      }

      case 12: {
        this.getSystems().manipulator.setIntakeSetPoint(-1);
        break;
      }

    }
  }


}
