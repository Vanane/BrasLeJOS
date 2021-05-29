 package com.RobotArm.business;
 
 import lejos.hardware.BrickFinder;
 import lejos.hardware.sensor.EV3ColorSensor;
 import lejos.hardware.sensor.SensorMode;
 
 
 public class CapteurCouleur
 {
   private EV3ColorSensor sensor;
   private SensorMode mode;
   private char port;
   
   public CapteurCouleur(char port) {
     this.sensor = new EV3ColorSensor(BrickFinder.getDefault().getPort("S" + port));
     this.mode = this.sensor.getColorIDMode();
   }
 
 
   
   public String getMesure() {
     return String.valueOf(this.sensor.getColorID());
   }
 }
