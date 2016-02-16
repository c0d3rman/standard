package org.usfirst.frc4904.standard.subsystems.motor;


import org.usfirst.frc4904.standard.LogKitten;
import org.usfirst.frc4904.standard.custom.CustomPID;
import org.usfirst.frc4904.standard.subsystems.motor.speedmodifiers.IdentityModifier;
import org.usfirst.frc4904.standard.subsystems.motor.speedmodifiers.SpeedModifier;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;

public abstract class SensorMotor extends Motor {
	protected final CustomPID pid;
	protected final boolean rateMode;
	private boolean enablePID;
	protected double position;
	protected long lastUpdate;
	protected final PIDSource sensor;
	
	public SensorMotor(String name, boolean inverted, SpeedModifier slopeController, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		super(name, inverted, slopeController, motors);
		sensor.setPIDSourceType(PIDSourceType.kDisplacement);
		pid = new CustomPID(0.0, 0.0, 0.0, sensor);
		this.sensor = sensor;
		enablePID = false;
		this.rateMode = rateMode;
	}
	
	public SensorMotor(String name, boolean isInverted, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this(name, isInverted, new IdentityModifier(), sensor, rateMode, motors);
	}
	
	public SensorMotor(String name, SpeedModifier slopeController, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this(name, false, slopeController, sensor, rateMode, motors);
	}
	
	public SensorMotor(String name, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this(name, false, new IdentityModifier(), sensor, rateMode, motors);
	}
	
	public SensorMotor(boolean isInverted, SpeedModifier speedModifier, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this("SensorMotor", isInverted, speedModifier, sensor, rateMode, motors);
	}
	
	public SensorMotor(boolean isInverted, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this("SensorMotor", isInverted, sensor, rateMode, motors);
	}
	
	public SensorMotor(SpeedModifier speedModifier, PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this("SensorMotor", speedModifier, sensor, rateMode, motors);
	}
	
	public SensorMotor(PIDSource sensor, boolean rateMode, SpeedController... motors) {
		this("SensorMotor", sensor, rateMode, motors);
	}
	
	public void reset() {
		pid.reset();
		position = sensor.pidGet();
	}
	
	public void setPID(double P, double I, double D) {
		pid.setPID(P, I, D);
	}
	
	public void setPIDF(double P, double I, double D, double F) {
		pid.setPID(P, I, D, F);
		LogKitten.d("P:" + P + "I:" + I + "D:" + D + "F:" + F);
	}
	
	public void setInputRange(double minimum, double maximum) {}
	
	public void enablePID() {
		enablePID = true;
		pid.enable();
	}
	
	public void disablePID() {
		enablePID = false;
		pid.disable();
	}
	
	public void setPosition(double position) {
		pid.setSetpoint(position);
		pid.enable();
		super.set(pid.get());
	}
	
	@Override
	public abstract void set(double speed);
	
	public void write(double speed) {
		if (enablePID) {
			if (rateMode) {
				super.set(pid.get() + speed);
			} else {
				super.set(pid.get());
			}
		} else {
			super.set(speed);
		}
	}
	
	@Override
	public void pidWrite(double speed) {
		LogKitten.v(Double.toString(speed));
		super.set(speed);
	}
}
