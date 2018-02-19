package com.digitalgoats.framework;

import com.digitalgoats.util.LogitechF310;

public interface ISystem {

  public void systemUpdate();
  public void execTeleop(LogitechF310 driver, LogitechF310 operator);
  public void execDisabled();
  public String getSystemName();

}
