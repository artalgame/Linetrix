package com.flaxtreme.tetroid;

import java.util.HashMap;

import android.content.Context;

import com.flurry.android.FlurryAgent;

public class Flurry {

	private Context context;
	
	public Flurry(Context appContext){
		context = appContext;
	}
	
	public void perform(FlurryMode mode, double value){
		
		HashMap<String, String> s = new HashMap<String, String>(1);
		//Log.d("desu", context.toString() + " " + mode.toString());
		switch (mode) {
		case START:
			FlurryAgent.setLogEnabled(false);
			FlurryAgent.setUseHttps(false);
			FlurryAgent.setContinueSessionMillis(1000 * 60);
			FlurryAgent.setReportLocation(true);
			FlurryAgent.onStartSession(context, "G4JVFRSJWNFBDYHVFZSB");	
			break;

		case STOP:
			FlurryAgent.onEndSession(context);
			break;
			
		case GAME_START:
			FlurryAgent.logEvent("GameSession", true);
			break;
			
		case GAME_OVER:
			FlurryAgent.logEvent("GameSession");
			break;
			
		case GAME_CLASSIC:
			s.put("Mode", "Classic");
			FlurryAgent.logEvent("GameStyle", s);
			break;
			
		case GAME_LINES:
			s.put("Mode", "Lines");
			FlurryAgent.logEvent("GameStyle", s);
			break;
			
		case SCREEN_CREDITS:
			FlurryAgent.logEvent("ScreenCredits");
			break;
						
		default:
			break;
		}
	}	
	
}
