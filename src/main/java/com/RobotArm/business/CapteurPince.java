package com.RobotArm.business;

import com.RobotArm.interfaces.ICapteur;

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
		//TODO : impl�menter la logique pour d�terminer si la pince doit s'arr�ter.
		return 0;
	}

}
