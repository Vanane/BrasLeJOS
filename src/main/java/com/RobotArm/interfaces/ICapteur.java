package com.RobotArm.interfaces;

/**
 * Interface impl�ment�e par les capteurs du robot
 * @author Alvin
 *
 */
public interface ICapteur {
	/**
	 * @return 1 si le capteur d�tecte les conditions pour arr�ter un moteur, 0 sinon
	 */
  int getMesure();
}
