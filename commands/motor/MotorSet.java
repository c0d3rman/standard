package org.usfirst.frc4904.standard.commands.motor;


import org.usfirst.frc4904.logkitten.LogKitten;
import org.usfirst.frc4904.standard.subsystems.motor.Motor;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Sets a motor to a speed.
 * The speed can change through
 * use of the set command.
 * This is better than setting
 * the motor because it uses
 * requires to avoid having
 * multiple attempts to set a
 * motor simultaneously.
 *
 */
public class MotorSet extends Command {
	private final SpeedController motor;
	private double speed;
	
	public MotorSet(Motor motor) {
		super("MotorSet");
		this.motor = motor;
		speed = 0;
		LogKitten.v("MotorSet created for " + motor.getName());
		requires(motor);
		setInterruptible(true);
	}
	
	protected void initialize() {
		LogKitten.v("MotorSet initialized");
	}
	
	/**
	 * Set the speed of the motor
	 */
	public void set(double speed) {
		this.speed = speed;
		LogKitten.d("MotorSet writePipe set to " + Double.toString(speed));
	}
	
	protected void execute() {
		motor.set(speed);
		LogKitten.d("MotorSet executing with speed " + Double.toString(speed));
	}
	
	protected void end() {
		motor.set(0);
		LogKitten.v("MotorSet ended (motor speed set to 0)");
	}
	
	protected void interrupted() {
		LogKitten.w("MotorSet interupted (motor speed undefined)");
	}
	
	protected boolean isFinished() {
		return false;
	}
}
