package com.digitalgoats.robot;

import com.digitalgoats.framework.ISystem;
import com.digitalgoats.systems.*;
import com.digitalgoats.util.LogitechF310;
import java.util.ArrayList;

public class SystemGroup {

  private ArrayList<ISystem> systems;
  public Camera camera;
  public Drive drive;
  public Elevator elevator;
  public Gyro gyro;
  public Manipulator manipulator;

  public SystemGroup() {

    camera = new Camera();
    drive = new Drive();
    elevator = new Elevator();
    gyro = new Gyro();
    manipulator = new Manipulator();

    this.setSystems(new ArrayList<ISystem>());
    this.getSystems().add(camera);
    this.getSystems().add(drive);
    this.getSystems().add(elevator);
    this.getSystems().add(gyro);
    this.getSystems().add(manipulator);

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
