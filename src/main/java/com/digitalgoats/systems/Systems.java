package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort.Port;
import java.util.ArrayList;

/**
 * This class combines all of the individual systems
 */
public class Systems implements IGoatSystem {

  // Objects
  public AHRS navx;
  public ArrayList<IGoatSystem> systems;
  public Drive drive;

  /**
   * Add all systems to
   */
  public Systems() {

    systems = new ArrayList<IGoatSystem>();
    navx = new AHRS(Port.kMXP);
    drive = new Drive(navx);

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
