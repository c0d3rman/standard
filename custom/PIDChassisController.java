package org.usfirst.frc4904.standard.custom;


import org.usfirst.frc4904.standard.custom.sensors.NavX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SerialPort;

public class PIDChassisController implements ChassisController, PIDOutput {
	private ChassisController controller;
	private double maxDegreesPerSecond;
	private double targetYaw;
	private double lastUpdate;
	private NavX ahrs;
	private double pidResult;
	public static PIDController pid;
	
	public PIDChassisController(ChassisController controller, double Kp, double Ki, double Kd, double maxDegreesPerSecond) {
		this.controller = controller;
		this.maxDegreesPerSecond = maxDegreesPerSecond;
		ahrs = new NavX(SerialPort.Port.kMXP);
		ahrs.reset();
		ahrs.resetDisplacement();
		ahrs.setPIDSourceType(PIDSourceType.kDisplacement);
		pid = new PIDController(Kp, Ki, Kd, ahrs, this);
		pid.setInputRange(-180.0f, 180.0f);
		pid.setOutputRange(-1.0f, 1.0f);
		pid.setContinuous(true);
		pid.reset();
		pid.enable();
		targetYaw = ahrs.getYaw();
	}
	
	public void reset() {
		targetYaw = ahrs.getYaw();
		pid.reset();
		pid.enable();
	}
	
	@Override
	public double getX() {
		return controller.getX();
	}
	
	@Override
	public double getY() {
		return controller.getY();
	}
	
	@Override
	public double getTurnSpeed() {
		targetYaw = targetYaw + ((controller.getTurnSpeed() * maxDegreesPerSecond) * ((System.currentTimeMillis() * 1000) - lastUpdate));
		lastUpdate = System.currentTimeMillis() * 1000;
		if (targetYaw > 180) {
			targetYaw = -180 + (Math.abs(targetYaw) - 180);
		} else if (targetYaw < -180) {
			targetYaw = 180 - (Math.abs(targetYaw) - 180);
		}
		pid.setSetpoint(targetYaw);
		return pidResult;
	}
	
	@Override
	public void pidWrite(double pidOut) {
		pidResult = pidOut;
		pidResult = Math.max(Math.min(pidResult, 1), -1);
	}
}
