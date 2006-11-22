package virium;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.BasicComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * TODO: Document this class
 *
 * @author kevin
 */
public class TitleState extends BasicGameState implements ComponentListener {
	public static final int ID = 2;
	
	private Image bg;
	private Image logo;
	private MouseOverArea p1;
	private MouseOverArea p2;
	private MouseOverArea quit;
	private MouseOverArea play;
	private GameContainer container;
	
	private String[] controls = new String[] {"Unplayed", "Keyboard", "Gamepad"};
	private String[] subtext1 = new String[] {"", "(Cursors, Ctrl)", "Hit Fire to Configure"};
	private String[] subtext2 = new String[] {"", "(W,A,S,D, V/Ctrl)", "Hit Fire to Configure"};
	
	private int controlType1 = 1;
	private int controller1 = 0;
	private int controlType2 = 0;
	private int controller2 = 0;
	
	private Font font;
	private StateBasedGame game;
	private boolean waitingForButton1;
	private boolean waitingForButton2;
	
	/**
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.container = container;
		this.game = game;
		
		font = new AngelCodeFont("res/font.fnt","res/font_00.tga");
		
		bg = new Image("res/background.png");
		logo = new Image("res/logo.png");
		p1 = new MouseOverArea(container,new Image("res/player1.png"),120,200,this);
		p1.setNormalColor(new Color(0.7f,0.7f,0.7f,1f));
		p1.setMouseOverColor(new Color(0.9f,0.9f,0.9f,1f));
		p2 = new MouseOverArea(container,new Image("res/player2.png"),470,200,this);
		p2.setNormalColor(new Color(0.7f,0.7f,0.7f,1f));
		p2.setMouseOverColor(new Color(0.9f,0.9f,0.9f,1f));
		quit = new MouseOverArea(container,new Image("res/quit.png"),350,520,this);
		quit.setNormalColor(new Color(0.7f,0.7f,0.7f,1f));
		quit.setMouseOverColor(new Color(0.9f,0.9f,0.9f,1f));
		play = new MouseOverArea(container,new Image("res/play.png"),340,430,this);
		play.setNormalColor(new Color(0.7f,0.7f,0.7f,1f));
		play.setMouseOverColor(new Color(0.9f,0.9f,0.9f,1f));
	}

	/**
	 * @see org.newdawn.slick.state.GameState#render(org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	public void render(StateBasedGame game, Graphics g) throws SlickException {
		bg.draw(0,0,800,600);
		logo.draw(250,30);
		
		p1.render(container, g);
		font.drawString(220-(font.getWidth(controls[controlType1])/2),300,controls[controlType1]);
		if ((controlType1 != 2) || (waitingForButton1)) {
			font.drawString(220-(font.getWidth(subtext1[controlType1])/2),345,subtext1[controlType1],Color.yellow);
		}
		
		p2.render(container, g);
		font.drawString(570-(font.getWidth(controls[controlType2])/2),300,controls[controlType2]);
		if ((controlType2 != 2) || (waitingForButton2)) {
			font.drawString(570-(font.getWidth(subtext2[controlType2])/2),345,subtext2[controlType2],Color.yellow);
		}
		
		quit.render(container, g);
		play.render(container, g);
	}

	/**
	 * @see org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
	}

	/**
	 * @see org.newdawn.slick.state.BasicGameState#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			container.exit();
		}
	}
	
	public void controllerButtonPressed(int controller, int button) {
		if (waitingForButton1) {
			waitingForButton1 = false;
			controller1 = controller;
		}
		if (waitingForButton2) {
			waitingForButton2 = false;
			controller2 = controller;
		}
	}

	/**
	 * @see org.newdawn.slick.gui.ComponentListener#componentActivated(org.newdawn.slick.gui.BasicComponent)
	 */
	public void componentActivated(BasicComponent source) {
		if (source == quit) {
			container.exit();
		}
		if (source == play) {
			game.enterState(InGameState.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
		if (source == p1) {
			controlType1 = (controlType1+1)%3;
			waitingForButton1 = false;
			if (controlType1 == 2) {
				waitingForButton1 = true;
				waitingForButton2 = false;
			} 
		}
		if (source == p2) {
			controlType2 = (controlType2+1)%3;
			waitingForButton2 = false;
			if (controlType2 == 2) {
				waitingForButton2 = true;
				waitingForButton1 = false;
			}
		}
	}

}