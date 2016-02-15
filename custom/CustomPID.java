package org.usfirst.frc4904.standard.custom;


import edu.wpi.first.wpilibj.PIDSource;

public class CustomPID {
	protected double P;
	protected double I;
	protected double D;
	protected double F;
	protected final PIDSource source;
	protected double setpoint;
	protected double totalError;
	protected double lastError;
	protected double lastUpdate;
	
	public CustomPID(double P, double I, double D, double F, PIDSource source) {
		this.P = P;
		this.I = I;
		this.D = D;
		this.F = F;
		this.source = source;
		this.setpoint = source.pidGet();
	}
	
	public CustomPID(double P, double I, double D, PIDSource source) {
		this(P, I, D, 0.0, source);
	}
	
	public void setPID(double P, double I, double D) {
		this.P = P;
		this.I = I;
		this.D = D;
	}
	
	public double get() {
		double input = source.pidGet();
		double error = (setpoint - input);/// ((double) ((System.currentTimeMillis() - lastUpdate)/1000));
		totalError += error;
		return P * error + I * totalError + D * (error - lastError) + F * setpoint;
	}
}
