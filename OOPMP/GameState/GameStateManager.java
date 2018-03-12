package GameState;


public class GameStateManager {

 private GameState[] gameStates;
 private int currentState;
//different gamestates
 public static final int NUMGAMESTATES = 4;
 public static final int MENUSTATE = 0;
 public static final int LEVEL1STATE = 1;
 public static final int LOADSTATE = 2;
 public static final int LEVEL2STATE = 3;

 public GameStateManager() {

  gameStates = new GameState[NUMGAMESTATES];

  currentState = MENUSTATE;
  loadState(currentState);

 }
//loadstate
 private void loadState(int state) {
  if (state == MENUSTATE)
   gameStates[state] = new MenuState(this);
  if (state == LEVEL1STATE)
   gameStates[state] = new Level1State(this);
  if (state == LOADSTATE)
   gameStates[state] = new LoadState(this);
  if (state == LEVEL2STATE)
   gameStates[state] = new Level2State(this);
 }
// unload the state so state can be switched
 private void unloadState(int state) {
  gameStates[state] = null;
 }
//switches state
 public void setState(int state) {
  unloadState(currentState);
  currentState = state;
  loadState(currentState);
 }
//calls update method for current state
 public void update() {
  try {
   gameStates[currentState].update();
  } catch (Exception e) {}
 }
//draws the state to graphics2d object
 public void draw(java.awt.Graphics2D g) {
  try {
   gameStates[currentState].draw(g);
  } catch (Exception e) {}
 }
//passes key pressed to currentgamestate
 public void keyPressed(int k) {
  try {
   gameStates[currentState].keyPressed(k);
  } catch (Exception e) {}
 }
//passes key pressed to currentgamestate
 public void keyReleased(int k) {
  try {
   gameStates[currentState].keyReleased(k);
  } catch (Exception e) {}
 }

}