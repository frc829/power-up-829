package com.digitalgoats.autos;

import com.digitalgoats.framework.Auto;
import com.digitalgoats.robot.SystemGroup;

public class DoNothingAuto extends Auto {

  public DoNothingAuto(SystemGroup systems) {
    super("Do Nothing", systems);
  }

  @Override
  public void execute() {

  }

}
