
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

public class Game {
	private int VBOVertexHandle;
	private int VBOColorHandle;
	public static GameState CurrentState = GameState.State_Menu;

	public void Start() {
		try {
			CreateWindow();
			InitGL();

			Run();
		} catch (Exception e) {

			e.printStackTrace();
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
		Display.setTitle("SWIGGITY SWAG");
		Display.create();
	}

	private void InitGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GLU.gluPerspective(45.0f, (float) displayMode.getWidth()
				/ (float) displayMode.getHeight(), 0.1f, 300.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	private void Run() {
		CreateVBO();
		float RotateYaw=0;
		while (!Display.isCloseRequested()) {
			try {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
						| GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glLoadIdentity();

				GL11.glTranslatef((float)Math.sin(RotateYaw/180*Math.PI), 0f, -4f); // Move Right 1.5 Units And Into
												// The
				GL11.glRotatef(45f, 0.4f, 1.0f, 0.1f);
				GL11.glRotatef(RotateYaw, 1f, 1.0f, 1f);
				RotateYaw+=2;
				DrawVBO();
				// Render();
				Display.update();
				Display.sync(60);
			} catch (Exception e) {

			}
		}
		Display.destroy();

	}

	private void DrawVBO() {
		GL11.glPushMatrix();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
		GL11.glPopMatrix();
	}

	private void CreateVBO() {
		VBOColorHandle = GL15.glGenBuffers();
		VBOVertexHandle = GL15.glGenBuffers();
		FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer(24 * 3);
		VertexPositionData.put(new float[] {
				1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				-1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				
				1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,
				
				1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				
				1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, -1.0f,
				-1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, -1.0f,
				
				-1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f,
				-1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f, 1.0f,
				
				1.0f, 1.0f, -1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, -1.0f
				});
		VertexPositionData.flip();
		FloatBuffer VertexColorData = BufferUtils.createFloatBuffer(24 * 3);
		VertexColorData.put(new float[] { 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1,1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, });
		VertexColorData.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOVertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexPositionData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBOColorHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, VertexColorData,
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public static void main(String[] args) throws LWJGLException {
		Game r = new Game();
		r.Start();
	}

	public enum GameState {
		State_Menu(0), State_SinglePlayer(1), State_Multiplayer(2), State_Editor(
				3);
		private int StateID;

		GameState(int i) {
			StateID = i;
		}

		public int GetID() {
			return StateID;
		}
	}
}