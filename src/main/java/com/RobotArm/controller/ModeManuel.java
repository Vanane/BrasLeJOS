package com.RobotArm.controller;

import com.RobotArm.interfaces.IEtatMode;

public class ModeManuel implements IEtatMode
{
	public ModeManuel()
	{
		
	}
	
	
	public boolean peutExecuter() { return true; }


	public boolean estAutonome() { return true; }

}