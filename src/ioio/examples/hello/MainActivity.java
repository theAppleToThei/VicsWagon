package ioio.examples.hello;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ToggleButton;

/**
 * This is the main activity of the HelloIOIO example application. It displays a toggle button on the screen, which
 * enables control of the on-board LED. Modified by Vic rev 131122A
 */
public class MainActivity extends IOIOActivity
{
	private ToggleButton button_;

	/**
	 * Called when the activity is first created. Here we normally initialize our GUI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button_ = (ToggleButton) findViewById(R.id.button);
	}

	/**
	 * This is the thread on which all the IOIO activity happens. It will be run every time the application is resumed
	 * and aborted when it is paused. The method setup() will be called right after a connection with the IOIO has been
	 * established (which might happen several times!). Then, loop() will be called repetitively until the IOIO gets
	 * disconnected
	 */
	class Looper extends BaseIOIOLooper
	{
		private DigitalOutput led_;// The on-board LED
		private DigitalOutput motorEnable; // Both motors
		private DigitalOutput rightMotorClock; // Step right motor
		private DigitalOutput leftMotorClock; // Step left motor
		private DigitalOutput motorCongtrollerReset;
		private DigitalOutput rightMotorControl; // Motor decay mode
		private DigitalOutput rightMotorDirection;
		private DigitalOutput leftMotorDirection;

		/**
		 * Called every time a connection with IOIO has been established. Typically used to open pins.
		 * 
		 * @throws ConnectionLostException
		 *             when IOIO connection is lost. when IOIO connection is lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#setup()
		 */
		@Override
		protected void setup() throws ConnectionLostException
		{
			led_ = ioio_.openDigitalOutput(0, true);
			rightMotorDirection = ioio_.openDigitalOutput(20, true);
			leftMotorDirection = ioio_.openDigitalOutput(21, false);
			motorCongtrollerReset = ioio_.openDigitalOutput(22, true);
			motorEnable = ioio_.openDigitalOutput(3, true);// Must be true for motors to run
			rightMotorClock = ioio_.openDigitalOutput(28, false);// Each pulse moves motor one step
			leftMotorClock = ioio_.openDigitalOutput(27, false);// Each pulse moves motor one step
			rightMotorControl = ioio_.openDigitalOutput(6, false);// Both motors, low => fast motor decay mode
		}

		/**
		 * Called repetitively while the IOIO is connected.
		 * 
		 * @throws ConnectionLostException
		 *             when IOIO connection is lost.
		 * @see ioio.lib.util.AbstractIOIOActivity.IOIOThread#loop()
		 */
		@Override
		public void loop() throws ConnectionLostException
		{
			if (!button_.isChecked())
			{
				led_.write(false);
				try
				{
					Thread.sleep(100);
					rightMotorClock.write(true);
					rightMotorClock.write(false);
					leftMotorClock.write(true);
					leftMotorClock.write(false);
				} catch (InterruptedException e)
				{
				}
			} else
			{
				led_.write(false);
			}
		}
	}

	/**
	 * A method to create our IOIO thread.
	 * 
	 * @see ioio.lib.util.AbstractIOIOActivity#createIOIOThread()
	 */
	@Override
	protected IOIOLooper createIOIOLooper()
	{
		return new Looper();
	}
}