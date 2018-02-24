package com.digitalgoats.framework;

import com.digitalgoats.util.LogitechF310;

public interface ISystem {

  public void autoInit();
  public void shuffleboard();
  public void teleopInit();
  public void teleopUpdate(LogitechF310 driver, LogitechF310 operator);
  public void update();

}
