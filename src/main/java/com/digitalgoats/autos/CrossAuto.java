package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class MiddleAuto extends Auto {

  Config config;
  EncoderFollower leftFollower, rightFollower;
  TankModifier modifier;
  Trajectory trajectory;
  Waypoint[] waypoints;

  public MiddleAuto(SystemGroup systems) {

    super("Middle Auto", systems);

    waypoints = new Waypoint[] {
    };
    config = new Config(FitMethod.HERMITE_CUBIC, Config.SAMPLES_HIGH, .05, 1.7, 2, 60);
    trajectory = Pathfinder.generate(waypoints, config);

    modifier = new TankModifier(trajectory).modify(.5);
    leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
    rightFollower = new EncoderFollower(modifier.getRightTrajectory());

  }

  @Override
  public void execute() {
    switch (this.getStep()) {
    }
  }

}
