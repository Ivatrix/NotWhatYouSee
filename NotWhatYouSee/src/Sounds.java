import static org.lwjgl.openal.AL10.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.util.WaveData;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;

public class Sounds {

	public void Start() {
		try {
			CreateWindow();

			init();

			Run();
		} catch (Exception e) {

		}
	}

	DisplayMode displayMode;

	private void CreateWindow() throws Exception {
		Display.setFullscreen(false);
		DisplayMode d[] = Display.getAvailableDisplayModes();
		for (int i = 0; i < d.length; i++) {
			if (d[i].getWidth() == 640 && d[i].getHeight() == 480
					&& d[i].getBitsPerPixel() == 32) {
				displayMode = d[i];
				break;
			}
		}
		Display.setDisplayMode(displayMode);
		Display.setTitle("Sounds");
		AL.create();
		Display.create();
	}

	public static boolean destroy = true;

	public static final int NUM_BUFFERS = 6;
	public static final int WALKING = 0;
	public static final int RUNNING = 1;
	public static final int AMBI = 2;
	IntBuffer buffer = BufferUtils.createIntBuffer(NUM_BUFFERS);
	IntBuffer source = BufferUtils.createIntBuffer(256);
	FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(
			new float[] { 0.0f, 0.0f, 0.0f });

	private void init() {

		AL10.alGenBuffers(buffer);

		try {

			WaveData waveFile = WaveData.create(new BufferedInputStream(
					new FileInputStream("res/footsteps2.wav")));
			AL10.alBufferData(buffer.get(WALKING), waveFile.format,
					waveFile.data, waveFile.samplerate);
			waveFile.dispose();

			waveFile = WaveData.create(new BufferedInputStream(
					new FileInputStream("res/running.wav")));
			AL10.alBufferData(buffer.get(RUNNING), waveFile.format,
					waveFile.data, waveFile.samplerate);
			waveFile.dispose();

			waveFile = WaveData.create(new BufferedInputStream(
					new FileInputStream("res/ambiance.wav")));
			AL10.alBufferData(buffer.get(AMBI), waveFile.format, waveFile.data,
					waveFile.samplerate);
			waveFile.dispose();

		} catch (Exception e) {
			e.printStackTrace();
		}

		AL10.alGenSources(source);

		addSources(WALKING);
		addSources(RUNNING);
		addSources(AMBI);
	}

	private void addSources(int type) {

		try {
			AL10.alSourcei(source.get(type), AL_BUFFER, buffer.get(type));
			AL10.alSource3f(source.get(type), AL_POSITION, 0f, 0f, 0f);
			AL10.alSourcei(source.get(type), AL_LOOPING, AL10.AL_TRUE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Run() {
			alSourcef(source.get(AMBI), AL10.AL_GAIN,1.0f);		
			alSourcePlay(source.get(AMBI));	

		while (!Display.isCloseRequested()) {
			try {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
						| GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glLoadIdentity();

				
				
				while (Keyboard.next() == true) {
					if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
							&& Keyboard.isKeyDown(Keyboard.KEY_W)
							|| Keyboard.isKeyDown(Keyboard.KEY_A)
							|| Keyboard.isKeyDown(Keyboard.KEY_S)
							|| Keyboard.isKeyDown(Keyboard.KEY_D)) {
						alSourceStop(source.get(RUNNING));
						alSourcePlay(source.get(WALKING));

					} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
						if (Keyboard.isKeyDown(Keyboard.KEY_W)
								|| Keyboard.isKeyDown(Keyboard.KEY_A)
								|| Keyboard.isKeyDown(Keyboard.KEY_S)
								|| Keyboard.isKeyDown(Keyboard.KEY_D)) {
							alSourceStop(source.get(WALKING));
							alSourcePlay(source.get(RUNNING));
						}
					} else {
						alSourceStop(source.get(WALKING));
						alSourceStop(source.get(RUNNING));
					}
				}

				Display.update();
				Display.sync(60);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		AL.destroy();
		Display.destroy();
		System.exit(0);

	}

	public static void main(String[] args) throws LWJGLException {
		Sounds r = new Sounds();
		r.Start();
	}
}