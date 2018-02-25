package com.digitalgoats.autos;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Elevator;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import java.io.File;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  File leftCsv, rightCsv;

  EncoderFollower elevatorFollower, leftFollower, rightFollower;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      // Load trajectory from file and configure encoder followers
      case 0: {

        Trajectory elevatorTrajectory = Pathfinder.generate(new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(0.3048, 0, 0)
        }, new Config(
            FitMethod.HERMITE_CUBIC,
            Config.SAMPLES_HIGH,
            .05,
            1.2192,
            0.9144,
            18.288
        ));

        if (MatchData.getOwnedSide(GameFeature.SWITCH_NEAR) == OwnedSide.LEFT) {
          leftCsv = new File("/home/lvuser/traj/middle/left-left.csv");
          rightCsv = new File("/home/lvuser/traj/middle/left-right.csv");
        } else {
          leftCsv = new File("/home/lvuser/traj/middle/right-left.csv");
          rightCsv = new File("/home/lvuser/traj/middle/right-right.csv");
        }

        elevatorFollower = new EncoderFollower(elevatorTrajectory);
        leftFollower = new EncoderFollower(Pathfinder.readFromCSV(leftCsv));
        rightFollower = new EncoderFollower(Pathfinder.readFromCSV(rightCsv));

        elevatorFollower.configureEncoder(
            (int)this.getSystems().elevator.getElevatorPosition(),
            Elevator.ENC_COUNTS,
            Elevator.WHEEL_DIAMETER
        );
        elevatorFollower.configurePIDVA(8, 0, 0, 1/Elevator.MAX_V, 0);
        leftFollower.configureEncoder(
            (int)this.getSystems().drive.getLeftPosition(),
            Drive.ENC_COUNTS * 3,
            Drive.WHEEL_DIAMETER
        );
        leftFollower.configurePIDVA(8, 0, 0, 1/Drive.MAX_V_L, 0);
        rightFollower.configureEncoder(
            (int)this.getSystems().drive.getRightPosition(),
            Drive.ENC_COUNTS * 3,
            Drive.WHEEL_DIAMETER
        );
        rightFollower.configurePIDVA(8, 0, 0, 1/Drive.MAX_V_R, 0);

        this.nextStep();

        break;
      }

      // Execute trajectories
      case 1: {

        double left = leftFollower.calculate((int)this.getSystems().drive.getLeftPosition());
        double right = rightFollower.calculate((int)this.getSystems().drive.getRightPosition());
        double gyroHeading = this.getSystems().gyro.getAngle();
        double desiredHeading = Pathfinder.r2d(leftFollower.getHeading());
        double deltaHeading = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = .8 * (-1/80) * deltaHeading;
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setLeftSetPoint(left + turn);
        this.getSystems().drive.setRightSetPoint(right - turn);

        double elevator = elevatorFollower.calculate((int)this.getSystems().elevator.getElevatorPosition());
        this.getSystems().elevator.setElevatorControlMode(ControlMode.PercentOutput);
        this.getSystems().elevator.setElevatorSetPoint(elevator);

        if (Math.abs(left + turn) <= .05 && Math.abs(right - turn) <= .05 && Math.abs(elevator) <= .05) {
          this.nextStep();
        }

        break;
      }

      // Release cube
      case 2: {

        this.getSystems().manipulator.setIntakeSetPoint(1);
        this.getSystems().manipulator.setGripStatus(true);

        break;
      }

    }
  }

}
