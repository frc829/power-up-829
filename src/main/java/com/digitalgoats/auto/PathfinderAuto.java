package com.digitalgoats.auto;

import com.digitalgoats.systems.SystemsGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Config;
import jaci.pathfinder.Trajectory.FitMethod;
import jaci.pathfinder.Waypoint;

public class PathfinderAuto extends Auto {

  Waypoint[] points = new Waypoint[]{
      new Waypoint(1, 3, 90)
  };
  Config config =  new Config(FitMethod.HERMITE_CUBIC, Config.SAMPLES_HIGH, .05, 1.7, 2, 60);
  Trajectory trajectory = Pathfinder.generate(points, config);

  public PathfinderAuto(SystemsGroup systemsGroup) {
    super("Pathfinder Auto", systemsGroup);

  }

  @Override
  public void execute() {
  }

}
