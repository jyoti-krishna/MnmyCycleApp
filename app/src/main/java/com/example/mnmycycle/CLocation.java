package com.example.mnmycycle;
import android.location.Location;

public class CLocation extends Location {
    private boolean useMetric=false;
    public CLocation(Location location) {
        this(location,true);
    }
    public CLocation(Location location,boolean useMetric){
        super(location);
        this.useMetric=useMetric;
    }
    public boolean getUseMetric(){
        return this.useMetric;
    }
    public void setUseMetric(boolean useMetric){
        this.useMetric=useMetric;
    }

    @Override
    public float distanceTo(Location dest) {
        return super.distanceTo(dest);
    }

    @Override
    public double getAltitude() {
        return super.getAltitude();
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public float getAccuracy() {
        return super.getAccuracy();
    }
}
