package com.RobotArm.business;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.RobotArm.interfaces.IExecuteur;

public class ThreadGamme {
	public IExecuteur executeur;
	private ExecutorService execService;
	private Future execThread; // Ajout�

	public ThreadGamme(IExecuteur executeur) {
		this.executeur = executeur;
		execService = Executors.newSingleThreadExecutor();
	}

	public void executer(final Gamme g)
	{
	// Utilisation de l'ExecutorService pour d�l�guer l'ex�cution � un thread g�r� par Java.
		Future execThread = execService.submit(new Runnable()
			{
				public void run() {
					try
					{						
						g.executer();
						notifierObservateur();
					}
					catch(Exception e)
					{
						notifierObservateur();
					}
				}
			});
	}

	private void notifierObservateur() {
		executeur.notifierFinGamme();
	}

	public Boolean gammeEnCours() // Ajout�
	{
		return execThread.isDone();
	}
	
	public void stop()
	{
		//TODO : arr�ter l'ex�cution d'une t�che	
	}
}