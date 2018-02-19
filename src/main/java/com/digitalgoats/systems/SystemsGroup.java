package com.digitalgoats.systems;

import com.digitalgoats.util.LogitechF310;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource;
import edu.wpi.cscore.VideoSource.Kind;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.ArrayList;

/**
 * This class combines all of the individual systems
 * @author Blake
 */
public class SystemsGroup implements IGoatSystem {

  // Objects
  public AHRS navx;
  public Compressor compressor;

  // SystemsGroup
  public ArrayList<IGoatSystem> systems;
  public Arm arm;
  public Drive drive;
  public Manipulator manipulator;

  /**
   * Add all systems to SystemsGroup
   */
  public SystemsGroup() {

    // Setup Objects
    navx = new AHRS(SPI.Port.kMXP);
    navx.resetDisplacement();
    navx.resetDisplacement();
    compressor = new Compressor(SystemMap.MAN_PCM.getValue());

    // Setup SystemsGroup
    systems = new ArrayList<IGoatSystem>();
    arm = new Arm();
    drive = new Drive(navx);
    manipulator = new Manipulator();

    // Add Individual SystemsGroup
    systems.add(arm);
    systems.add(drive);
    systems.add(manipulator);

    // Start Compressor
    compressor.clearAllPCMStickyFaults();
    compressor.start();

  }

  public void startCamera() {
    CameraServer.getInstance().startAutomaticCapture().setVideoMode(PixelFormat.kYUYV, 320, 240, 15);
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
    compressor.start();
    for (IGoatSystem system : systems) {
      system.autonomousUpdateSystem();
    }
  }

  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {
    compressor.start();
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
