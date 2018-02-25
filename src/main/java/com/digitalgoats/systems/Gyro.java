package com.digitalgoats.systems;

import com.digitalgoats.framework.ISystem;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Gyro extends AHRS implements ISystem {

  public Gyro() {
    super(Port.kMXP);
  }

  // endregion

  // region Overridden

  @Override
  public void shuffleboard() {

    SmartDashboard.putNumber("Gyro: Angle", this.getAngle());
    SmartDashboard.putNumber("Gyro: X Displacement", this.getDisplacementX());
    SmartDashboard.putNumber("Gyro: Y Displacement", this.getDisplacementY());
    SmartDashboard.putNumber("Gyro: Z Displacement", this.getDisplacementZ());

  }

  // endregion

}
