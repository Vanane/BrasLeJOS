package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;

/**
 * Capteur reli� � la pince. La pince n'a pas de capteur, cette classe est factice
 * @author Alvin
 *
 */
public class CapteurPince implements ICapteur {

	private static CapteurPince capteur;
	
   public static CapteurPince getInstance()
   {
	   return capteur;
   }

   public static void initCapteur()
   {
	   if(capteur != null)
		   return;
	   capteur = new CapteurPince();
   }
	 	
	
	@Override
	public int getMesure()
	{
		//TODO trouver une logique plus int�ressante pour que la pince soit arr�t�e lorsqu'elle force
		return 0;
	}

}
