package com.flaxtreme.tetroid;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class ActionResolver implements IActionResolver {

	private Context context;
	Handler uiThread;
	private Flurry f;
	
	public ActionResolver(Context context){
		this.context = context;
		uiThread = new Handler();
		f = new Flurry(context);
	}
		
	@Override
	public void dispose() {
		f.perform(FlurryMode.STOP, 0);
	}

	@Override
	public void flurry(FlurryMode flurryMode, double value) {
		f.perform(flurryMode, value);		
	}

	@Override
	public void share(final int score) {
		
		uiThread.post(new Runnable() {			
			@Override
			public void run() {				
				String shareBody = GameApp.get().lang.get("Score", score);
				
				Intent intent;
				intent = new Intent(Intent.ACTION_SEND);
				intent.putExtra(Intent.EXTRA_TEXT, shareBody);
				intent.setType("text/plain");
				
				context.startActivity(Intent.createChooser(intent, GameApp.get().lang.get("Share via")));				
			}
		});
	}

}
