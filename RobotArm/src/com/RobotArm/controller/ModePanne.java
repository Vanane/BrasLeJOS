package com.RobotArm.controller;

import com.RobotArm.interfaces.IEtatMode;

public class ModePanne implements IEtatMode
{
	public ModePanne()
	{
		
	}
	
	
	public boolean peutExecuter() { return false; }


	public boolean estAutonome() { return false; }
}