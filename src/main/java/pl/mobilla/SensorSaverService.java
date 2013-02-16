package pl.mobilla;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class SensorSaverService extends Service implements SensorListener {

	private SensorManager systemService;
	private Handler handler;
	private WakeLock wakeLock;
	private boolean started;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if(started)
			return;
		
		started = true;

		systemService = (SensorManager) getSystemService(SENSOR_SERVICE);

		systemService.registerListener(this, Sensor.TYPE_ALL,
				SensorManager.SENSOR_DELAY_FASTEST);
		
		PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wakeLock.acquire();
		
		handler = new Handler();

	}

	@Override
	public void onDestroy() {
		systemService.unregisterListener(this);
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
		handler = null;
		wakeLock.release();
		started = false;
	}

	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(final int sensor, final float[] values) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				saveValues(sensor, values);
			}
		});
	}

	private void saveValues(int sensor, float[] values) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(
					new File(String.format("/mnt/sdcard/ski-service/ski-service.%d.csv",
							Calendar.getInstance().get(Calendar.HOUR_OF_DAY))).getAbsolutePath(),
					true);
			
			fileWriter.write(String.format("%d,%d,%s%s", System.currentTimeMillis(), sensor, formatArray(values), System.getProperty("line.separator")));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null)
				try {
					fileWriter.close();
				} catch (IOException e) {
				}
		}
	}

	private Object formatArray(float[] values) {
		StringBuilder stringBuilder = new StringBuilder();
		for(float f : values) {
			stringBuilder.append(f);
			stringBuilder.append(',');
		}
		return stringBuilder.toString();
	}

}
