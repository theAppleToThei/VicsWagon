package ioio.examples.hello;

/**************************************************************************
 * Happy version...ultrasonics working
 **************************************************************************/
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PulseInput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import android.os.SystemClock;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * An UltraSonicSensors instance is used to access three ultrasonic sensors
 * (leftInput, frontInput, and rightInput) and read the measurements from these
 * sensors. version 140427...modified by Vic...ultrasonics works using Ytai's
 * suggestions...cleaned up formatting
 * 
 * @author Erik Colban
 */
public class UltraSonicSensor {
	private IOIO ioio;
	private ToggleButton button_;
	private TextView log;
	private ScrollView scroller;
	private int frontDistance;
	private int rearDistance;
	private int leftDistance;
	private int rightDistance;
	private DigitalOutput frontStrobe;
	private DigitalOutput rearStrobe;
	private DigitalOutput leftStrobe;
	private DigitalOutput rightStrobe;
	private static final int FRONT_STROBE_ULTRASONIC_OUTPUT_PIN = 16;
	private static final int LEFT_STROBE_ULTRASONIC_OUTPUT_PIN = 17;
	private static final int RIGHT_STROBE_ULTRASONIC_OUTPUT_PIN = 15;
	private static final int FRONT_ULTRASONIC_INPUT_PIN = 12;
	private static final int REAR_ULTRASONIC_INPUT_PIN = 10;// input to ioio
	private static final int RIGHT_ULTRASONIC_INPUT_PIN = 11;
	private static final int LEFT_ULTRASONIC_INPUT_PIN = 13;
	private PulseInput leftInput;
	private float CONVERSION_FACTOR = 1000000; // Gives ultrasonics reqadings in
												// microseconds
	/**
	 * Constructor of a UltraSonicSensors instance.
	 * 
	 * @param ioio
	 *            the IOIO instance used to communicate with the sensor
	 * @throws ConnectionLostException
	 */
	public UltraSonicSensor(IOIO ioio) throws ConnectionLostException {
		this.ioio = ioio;
		this.leftStrobe = ioio
				.openDigitalOutput(LEFT_STROBE_ULTRASONIC_OUTPUT_PIN);// *******
//		 this.rightStrobe =
//		 ioio.openDigitalOutput(RIGHT_STROBE_ULTRASONIC_OUTPUT_PIN);// *******
//		 this.frontStrobe =
//		 ioio.openDigitalOutput(FRONT_STROBE_ULTRASONIC_OUTPUT_PIN);// *******
	}

	/**
	 * Makes a reading of the ultrasonic sensors and stores the results locally.
	 * To access these readings, use {@link #getLeftDistance()},
	 * {@link #getFrontDistance()}, and {@link #getRightDistance()}.
	 * 
	 * @throws ConnectionLostException
	 * @throws InterruptedException
	 */
	public void read() throws ConnectionLostException, InterruptedException {
		leftDistance = read(leftStrobe, leftInput, LEFT_ULTRASONIC_INPUT_PIN);
		// frontDistance = read(frontStrobe, frontInput,
		// FRONT_ULTRASONIC_INPUT_PIN);
		// rightDistance = read(righttStrobe, rightInput,
		// RIGHT_ULTRASONIC_INPUT_PIN);
	}

	private int read(DigitalOutput strobe, PulseInput input, int inputPin)
			throws ConnectionLostException, InterruptedException // Order of following statement is very important...do not change
	{
		int distance = 0;
		ioio.beginBatch();
		strobe.write(true);
		input = ioio.openPulseInput(inputPin, PulseMode.POSITIVE);
		ioio.endBatch();
		SystemClock.sleep(40);
		strobe.write(false);
		distance += (int) (input.getDuration() * CONVERSION_FACTOR);
		input.close();
		return distance;
	}

	public synchronized int getLeftDistance() {
		return leftDistance;
	}

	public synchronized int getFrontDistance() {
		return frontDistance;
	}

	public synchronized int getRightDistance() {
		return rightDistance;
	}

	/**
	 * Writes a message to the Dashboard instance.
	 */
	public void closeConnection() {
		leftInput.close();
		// frontInput.close();
		// rightInput.close();
		leftStrobe.close();
	}
}
