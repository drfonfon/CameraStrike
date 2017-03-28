package com.fonfon.camerastrike.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public final class ShakeDetector implements SensorEventListener {

    private final ShakeListener shakeListener;
    private final float threshold;
    private float accelerate;
    private float accelerateCurrent = SensorManager.GRAVITY_EARTH;
    private SensorManager sensorManager;

    public ShakeDetector(Context context, float threshold, ShakeListener shakeListener) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.shakeListener = shakeListener;
        this.threshold = threshold;
    }

    public final void start() {
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public final void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float accelerateLast = accelerateCurrent;
        accelerateCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = accelerateCurrent - accelerateLast;
        accelerate = accelerate * 0.9f + delta;
        if (accelerate > threshold) {
            shakeListener.onShakeDetected();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface ShakeListener {
        void onShakeDetected();
    }
}
