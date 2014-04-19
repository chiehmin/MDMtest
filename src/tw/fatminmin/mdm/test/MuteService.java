package tw.fatminmin.mdm.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.Toast;

public class MuteService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		if(!audioManager.isMicrophoneMute() && MainActivity.muted) {
			audioManager.setMicrophoneMute(true);
			Toast.makeText(this, "set mic mute", Toast.LENGTH_SHORT).show();
		}
		if(audioManager.isMicrophoneMute() && !MainActivity.muted) {
			audioManager.setMicrophoneMute(false);
			Toast.makeText(this, "set mic unmute", Toast.LENGTH_SHORT).show();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

}
