package kien.objects;

/**
 * Created by KIEN on 6/23/2015.
 */
public class LatLong {
    private float latitude;
    private float longtitude;

    public LatLong(float latitude, float longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }
}
