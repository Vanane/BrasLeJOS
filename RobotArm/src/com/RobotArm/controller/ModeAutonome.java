package com.RobotArm.controller;

import com.RobotArm.interfaces.IEtatMode;

public class ModeAutonome implements IEtatMode
{
	public ModeAutonome()
	{
		
	}
	
	
	public boolean peutExecuter() { return true; }


	public boolean estAutonome() { return false; }
	
}