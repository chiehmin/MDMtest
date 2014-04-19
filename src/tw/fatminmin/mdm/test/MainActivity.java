package tw.fatminmin.mdm.test;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	static public boolean muted;
	private ToggleButton btnMute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		btnMute = (ToggleButton) findViewById(R.id.btnMute);
		btnMute.setChecked(audioManager.isMicrophoneMute());
		btnMute.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				muted = btnMute.isChecked();
			}
		});
		
		
		Intent intent = new Intent(this, MuteService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar now = Calendar.getInstance();
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), 1000, pendingIntent);
		
		
		
		usb();
	}
	
	
	private void usb() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.addAction(Intent.ACTION_POWER_CONNECTED);
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		
		BroadcastReceiver usbReceiver = new BroadcastReceiver() {
			String now = "";
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
					if(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == BatteryManager.BATTERY_PLUGGED_USB) {
						now = "pc";
					}
					else {
						now = "charger";
					}
				}
				if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
					Toast.makeText(MainActivity.this, now, Toast.LENGTH_SHORT).show();
				}
				if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
					Toast.makeText(MainActivity.this, "usb attached", Toast.LENGTH_SHORT).show();
				}
				if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
					Toast.makeText(MainActivity.this, "usb detached", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		registerReceiver(usbReceiver, filter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
