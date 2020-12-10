package com.example.sonerimtestproject.model;

public class GeoPointModel {
    private double mLatitude;
    private double mLongitude;
    private double mAltitude = 0.0;

    public GeoPointModel(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public GeoPointModel(double latitude, double longitude, double altitude) {
        mLatitude = latitude;
        mLongitude = longitude;
        mAltitude = altitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double mAltitude) {
        this.mAltitude = mAltitude;
    }


    public static GeoPointModel fromTripleString(final String s, final char spacer) {
        final int spacerPos1 = s.indexOf(spacer);
        final int spacerPos2 = s.indexOf(spacer, spacerPos1 + 1);

        if (spacerPos2 == -1) {
            return new GeoPointModel(
                    (Double.parseDouble(s.substring(0, spacerPos1))),
                    (Double.parseDouble(s.substring(spacerPos1 + 1))),
                    (Double.parseDouble(s.substring(spacerPos2 + 1))));
        } else {
            return new GeoPointModel(
                    (Double.parseDouble(s.substring(0, spacerPos1))),
                    (Double.parseDouble(s.substring(spacerPos1 + 1, spacerPos2))),
                    (Double.parseDouble(s.substring(spacerPos2 + 1))));
        }
    }

    public String toTripleString() {
        return String.valueOf(this.mLatitude) + "," +
                this.mLongitude+ "," + this.mAltitude;
    }

    //try to get distance by Pifagor function
    public double getDistance(GeoPointModel geoPointModel){
        double cathetusB;
        double cathetusA;
        double hipotinuseC;

        cathetusB= Math.sqrt(Math.pow((geoPointModel.getLatitude() - mLatitude), 2) + Math.pow((geoPointModel.getLongitude() - mLongitude), 2));
        cathetusA = geoPointModel.getAltitude()>mAltitude ?  geoPointModel.getAltitude()-mAltitude : mAltitude - geoPointModel.getAltitude();
        hipotinuseC = Math.sqrt( Math.pow(cathetusB, 2)+ Math.pow(cathetusA, 2));

        return hipotinuseC;
    }

    public double distanceToAsDouble(final GeoPointModel other) {
        final double DEG2RAD = Math.PI / 180.0;
        final int RADIUS_EARTH_METERS = 6378137;

        final double lat1 = DEG2RAD * getLatitude();
        final double lat2 = DEG2RAD * other.getLatitude();
        final double lon1 = DEG2RAD * getLongitude();
        final double lon2 = DEG2RAD * other.getLongitude();
        return RADIUS_EARTH_METERS * 2 * Math.asin(Math.min(1, Math.sqrt(
                Math.pow(Math.sin((lat2 - lat1) / 2), 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin((lon2 - lon1) / 2), 2)
        )));
    }
}
