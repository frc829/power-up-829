package com.digitalgoats.robot;

import com.digitalgoats.framework.ISystem;
import com.digitalgoats.systems.*;
import com.digitalgoats.util.LogitechF310;
import java.util.ArrayList;

public class SystemGroup {

  private ArrayList<ISystem> systems;
  public Drive drive;
  public Elevator elevator;
  public Manipulator manipulator;

  public SystemGroup() {

    this.setSystems(new ArrayList<ISystem>());
    drive = new Drive();
    elevator = new Elevator();
    manipulator = new Manipulator();
    this.getSystems().add(new Drive());
    this.getSystems().add(new Elevator());
    this.getSystems().add(new Manipulator());

  }

  public void autoInit() {
    for (ISystem system : this.getSystems()) {
      system.autoInit();
    }
  }

  public void shuffleboard() {
    for (ISystem system : this.getSystems()) {
      system.shuffleboard();
    }
  }

  public void teleopInit() {
    for (ISystem system : this.getSystems()) {
      system.teleopInit();
    }
  }

  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator) {
    for (ISystem system : this.getSystems()) {
      system.teleopUpdate(driver, operator);
    }
  }

  public void update() {
    for (ISystem system : this.getSystems()) {
      system.update();
    }
  }

  public ArrayList<ISystem> getSystems() {
    return this.systems;
  }

  public void setSystems(ArrayList<ISystem> systems) {
    this.systems = systems;
  }

}
