package com.digitalgoats.systems;

import com.digitalgoats.framework.ISystem;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;

public class Camera implements ISystem {

  // region Objects

  private UsbCamera camera;

  // endregion

  public Camera() {

    this.setCamera(CameraServer.getInstance().startAutomaticCapture());
    this.getCamera().setVideoMode(PixelFormat.kYUYV, 160, 120, 15);

  }

  // region Getters & Setters

  public UsbCamera getCamera() {
    return this.camera;
  }

  public void setCamera(UsbCamera camera) {
    this.camera = camera;
  }

  // endregion

}
