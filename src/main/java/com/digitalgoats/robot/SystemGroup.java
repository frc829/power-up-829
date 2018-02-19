package com.digitalgoats.robot;

import com.digitalgoats.framework.ISystem;
import com.digitalgoats.systems.Drive;
import com.digitalgoats.util.LogitechF310;
import java.util.ArrayList;

public class SystemGroup {

  public ArrayList<ISystem> systems;
  public Drive drive;

  public SystemGroup() {

    systems = new ArrayList<ISystem>();

    drive = new Drive();
    systems.add(drive);

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
