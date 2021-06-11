package com.RobotArm.interfaces;

/**
 * Interface impl�ment�e par les diff�rents �tats du robot
 * @author Alvin
 *
 */
public interface IEtatMode {
	/**
	 * @return True si l'�tat autorise d'ex�cuter une gamme, False sinon
	 */
	boolean peutExecuter();
	
	/**
	 * @return True si l'�tat correspond � une ex�cution autonmatique, False sinon
	 */
	boolean estAutonome();
}
