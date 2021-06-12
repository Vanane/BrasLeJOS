package com.RobotArm.interfaces;

/**
 * Interface permettant d'�tre notifi� par le syst�me de pilotage
 * @author Alvin
 *
 */
public interface IPilote
{
	/**
	 * Appel�e lorsqu'un message est re�u par le syst�me de pilotage
	 * @param paramString
	 */
	void notifierMessage(String paramString);
}
