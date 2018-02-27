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
import jaci.pathfinder.modifiers.TankModifier;
import java.io.File;
import java.nio.file.Path;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;

public class MiddleAuto extends Auto {

  EncoderFollower elevatorFollower, leftFollower, rightFollower;

  public MiddleAuto(SystemGroup systems) {
    super("Middle Auto", systems);
  }

  @Override
  public void execute() {
    switch (this.getStep()) {

      case 0: {

        Trajectory elevatorTrajectory = Pathfinder.generate(new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(0.3084, 0, 0)
        }, new Config(
            FitMethod.HERMITE_CUBIC,
            Config.SAMPLES_FAST,
            .05,
            1.7,
            2,
            60
        ));

        Trajectory driveTrajectory = Pathfinder.generate(new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(10, 0, 0)
        }, new Config(
           FitMethod.HERMITE_CUBIC,
           Config.SAMPLES_FAST,
           .05,
           6,
           3,
           60
        ));
        TankModifier modifier = new TankModifier(driveTrajectory).modify(.517525);

        elevatorFollower = new EncoderFollower(elevatorTrajectory);
        try {
          File left = new File("/trajectories/mid-left_left_detailed.csv");
          File right = new File("/trajectories/mid-left_right_detailed.csv");
          Trajectory leftTraj = Pathfinder.readFromCSV(left);
          Trajectory rightTraj = Pathfinder.readFromCSV(right);
          leftFollower = new EncoderFollower(leftTraj);
          rightFollower = new EncoderFollower(rightTraj);
        } catch (Exception e) {
          leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
          rightFollower = new EncoderFollower(modifier.getRightTrajectory());
        }

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

      case 1: {

        double elevator = elevatorFollower.calculate((int)this.getSystems().elevator.getElevatorPosition());
        this.getSystems().elevator.setElevatorControlMode(ControlMode.PercentOutput);
        this.getSystems().elevator.setElevatorSetPoint(elevator);

        double gyro = this.getSystems().gyro.getAngle();
        double desired = Pathfinder.r2d(leftFollower.getHeading());
        double delta = Pathfinder.boundHalfDegrees(desired - gyro);
        double turn = .8 * (-1/80) * delta;

        double left = leftFollower.calculate((int)this.getSystems().drive.getLeftPosition());
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setLeftSetPoint(left + turn);

        double right = rightFollower.calculate((int)this.getSystems().drive.getRightPosition());
        this.getSystems().drive.setDriveControlMode(ControlMode.PercentOutput);
        this.getSystems().drive.setRightSetPoint(right - turn);

        System.out.println(elevator);
        System.out.println(left + turn);
        System.out.println(right - turn);

        if (elevator == 0) {
          this.getSystems().elevator.setElevatorSetPoint(.0625);
        }
        break;
      }

      case 2: {
        this.getSystems().elevator.setElevatorSetPoint(.0625);
        this.getSystems().manipulator.setIntakeSetPoint(1);
        this.getSystems().manipulator.setGripStatus(true);

        break;
      }

    }
  }

}
