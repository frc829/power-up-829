package com.digitalgoats.robot;

import com.digitalgoats.framework.ISystem;
import com.digitalgoats.systems.Manipulator;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.systems.Elevator;
import com.digitalgoats.util.LogitechF310;
import java.util.ArrayList;

public class SystemGroup {

  public ArrayList<ISystem> systems;
  public Drive drive;
  public Elevator elevator;
  public Manipulator manipulator;

  public SystemGroup() {

    systems = new ArrayList<ISystem>();

    drive = new Drive();
    elevator = new Elevator();
    manipulator = new Manipulator();

    systems.add(drive);
    systems.add(elevator);
    systems.add(manipulator);

  }

  // region System Functions

  public void systemsUpdate() {
    for (ISystem system : systems) {
      system.systemUpdate();
    }
  }

  public void teleopPeriodic(LogitechF310 driver, LogitechF310 operator) {
    for (ISystem system : systems) {
      system.execTeleop(driver, operator);
    }
  }

  public void disabledPeriodic() {
    for (ISystem system : systems) {
      system.execDisabled();
    }
  }

  // endregion

}
