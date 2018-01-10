package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SerialPort.Port;
import java.util.ArrayList;

/**
 * This class combines all of the individual systems
 */
public class Systems implements IGoatSystem {

  // Objects
  public AHRS navx;
  public Compressor compressor;

  // Systems
  public ArrayList<IGoatSystem> systems;
  public Drive drive;

  /**
   * Add all systems to
   */
  public Systems() {

    // Setup Objects
    navx = new AHRS(Port.kMXP);
    compressor = new Compressor(SystemMap.DRIVE_PCM.getValue());

    // Setup Systems
    systems = new ArrayList<IGoatSystem>();
    drive = new Drive(navx);

    // Add Individual Systems
    systems.add(drive);

    // Start Compressor
    compressor.clearAllPCMStickyFaults();
    compressor.start();

  }

  @Override
  public void disabledUpdateSystem() {
    compressor.stop();
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
