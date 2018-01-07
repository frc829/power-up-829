package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;
import java.util.ArrayList;

/**
 * This class combines all of the individual systems
 */
public class Systems implements IGoatSystem {

  // Objects
  public ArrayList<IGoatSystem> systems = new ArrayList<IGoatSystem>();
  public Drive drive;

  /**
   * Add all systems to
   */
  public Systems() {

    // Add Individual Systems
    systems.add(drive);

  }

  @Override
  public void disabledUpdateSystem() {
    for (IGoatSystem system : systems) {
      system.disabledUpdateSystem();
    }
  }

  @Override
  public void autonomousUpdateSystem() {
    for (IGoatSystem system : systems) {
      system.autonomousUpdateSystem();
    }
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {
    for (IGoatSystem system : systems) {
      system.teleopUpdateSystem(driver, operator);
    }
  }

  @Override
  public void updateSmartDashboard() {
    for (IGoatSystem system : systems) {
      system.updateSmartDashboard();
    }
  }

  @Override
  public String getSystemName() {
    return "Systems Group";
  }

}
