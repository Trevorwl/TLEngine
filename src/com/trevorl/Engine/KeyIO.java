package com.trevorl.Engine;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * A KeyListener which provides improved
 * functionality over Java's
 * keystroke library. 
 * All keystrokes are registered, and
 * callbacks are called for as long as a key is
 * pressed. Key press and release handlers can
 * be enabled or disabled depending on the state of a client
 * program. This class allows a program to scan for inputs at
 * customized intervals by using a timer to call scanInputs().
 * This avoids the unpredictability
 * of a client directly registering key inputs
 * using Java's library. 
 * 
 * Author: Trevor Lash
 * Modified: 1/1/2022
 */
public class KeyIO<T> implements KeyListener{
	
	/**
	 * Key mappings 
	 */
	private ConcurrentHashMap<Integer, T> keyAssignments;
	
	/**
	 * Flags used to signal whether or not a key is pressed
	 */
	private ConcurrentHashMap<Integer, Boolean> keysPressed;
	
	/**
	 * Event mapping of handlers to keys. 
	 * Called for as long as a key is pressed.
	 */
	private ConcurrentHashMap<Integer, Consumer<T>>pressHandlers;
	
	/**
	 * Event mapping of handlers to keys. 
	 * Called when a key is released.
	 */
	private ConcurrentHashMap<Integer, Consumer<T>>releaseHandlers;
	
	/**
	 * A list of press-handlers which are currently disabled
	 */
	private ConcurrentLinkedQueue<Integer>disabledPressHandlers;
	
	/**
	 * A list of release-handlers which are currently disabled
	 */
	private ConcurrentLinkedQueue<Integer>disabledReleaseHandlers;
	
	private ConcurrentHashMap<Integer, Boolean>oncePerPress;

	public KeyIO(ConcurrentHashMap<Integer, T> keyAssignments) {
		
		this.keyAssignments = keyAssignments;

		keysPressed = new ConcurrentHashMap<>();
		pressHandlers = new ConcurrentHashMap<>();
		releaseHandlers = new ConcurrentHashMap<>();
		disabledPressHandlers = new ConcurrentLinkedQueue<>();
		disabledReleaseHandlers = new ConcurrentLinkedQueue<>();
		oncePerPress = new ConcurrentHashMap<>();
		
		for(Integer keyCode : keyAssignments.keySet()) {
			keysPressed.put(keyCode, false);
		}
	}
	
	public void addPressHandler(Integer keyCode, 
			Consumer<T> handler) {
		
		pressHandlers.put(keyCode, handler);
	}
	
	public void readPressOnce(Integer keyCode) {
		if(!oncePerPress.containsKey(keyCode)) {
			oncePerPress.put(keyCode, false);
		}
	}
	
	public void readPressAlways(Integer keyCode) {
		if(oncePerPress.containsKey(keyCode)) {
			oncePerPress.remove(keyCode);
		}
	}
	
	public void addReleaseHandler(Integer keyCode, 
			Consumer<T> handler) {
		
		releaseHandlers.put(keyCode, handler);
	}
	
	public void disablePressHandler(Integer keyCode) {
		if(!pressHandlerIsDisabled(keyCode)) {
			disabledPressHandlers.add(keyCode);
		}
	}
	
	public void disableReleaseHandler(Integer keyCode) {
		if(!releaseHandlerIsDisabled(keyCode)) {
			disabledPressHandlers.add(keyCode);
		}
	}
	
	public void disableAllPressHandlers() {
		for(Integer keyCode : keyAssignments.keySet()) {
			disablePressHandler(keyCode);
		}
	}
	
	public void disableAllReleaseHandlers() {
		for(Integer keyCode : keyAssignments.keySet()) {
			disableReleaseHandler(keyCode);
		}
	}
	
	public void enablePressHandler(Integer keyCode) {
		if(pressHandlerIsDisabled(keyCode)) {
			disabledPressHandlers.remove(keyCode);
		}
	}
	
	public void enableReleaseHandler(Integer keyCode) {
		if(releaseHandlerIsDisabled(keyCode)) {
			disabledPressHandlers.remove(keyCode);
		}
	}
	
	public void enableAllPressHandlers() {
		for(Integer keyCode : keyAssignments.keySet()) {
			enablePressHandler(keyCode);
		}
	}
	
	public void enableAllReleaseHandlers() {
		for(Integer keyCode : keyAssignments.keySet()) {
			enableReleaseHandler(keyCode);
		}
	}
	
	public void disableAllHandlers() {
		disableAllPressHandlers();
		disableAllReleaseHandlers();
	}
	
	public void enableAllHandlers() {
		enableAllPressHandlers();
		enableAllReleaseHandlers();
	}
	
	public boolean releaseHandlerIsDisabled(Integer keyCode) {
		return disabledReleaseHandlers.contains(keyCode);
	}
	
	public boolean pressHandlerIsDisabled(Integer keyCode) {
		return disabledPressHandlers.contains(keyCode);
	}
	
	/**
	 * Scans the key map for pressed keys.
	 * If a key is pressed and its press handler 
	 * is not disabled, the handler is called.
	 * This method is called at customized intervals
	 * by the client program.
	 */
	public void scanInputs() {
		for(Integer keyCode : keyAssignments.keySet()) {
			if(keysPressed.get(keyCode)) {
				if(pressHandlers.containsKey(keyCode) 
						&& !pressHandlerIsDisabled(keyCode)) {
					
					if(oncePerPress.containsKey(keyCode)) {
						if(oncePerPress.get(keyCode)) {
							continue;
						}
						
						oncePerPress.put(keyCode, true);
					}
					
					T binding = keyAssignments.get(keyCode);
	
					pressHandlers.get(keyCode).accept(binding);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(keysPressed.containsKey(e.getKeyCode())) {
			keysPressed.put(e.getKeyCode(), true);
		}
	}

	/**
	 * If the release handler
	 * for the released key is 
	 * not disabled, it is called.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keysPressed.containsKey(keyCode)) {
			keysPressed.put(keyCode, false);
			
			if(releaseHandlers.containsKey(keyCode) 
					&& !releaseHandlerIsDisabled(keyCode)) {
				
				T binding = keyAssignments.get(keyCode);
				
				releaseHandlers.get(keyCode).accept(binding);
			}
			
			if(oncePerPress.containsKey(keyCode)) {
				oncePerPress.put(keyCode, false);
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
}
