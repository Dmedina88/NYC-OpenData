package com.grayherring.nyc_opendata.models;

/**
 * Created by David on 4/6/2015.
 */
public class MinMaxLatLon{

        public static final double KILOMETER_MILE = 1.60934;
        public static final double METER_MILE = KILOMETER_MILE*1000;

        public double minLat;
        public double minLon;
        public double maxLat;
        public double maxLon;

        public MinMaxLatLon(double lat, double lon, long radius){
            double radiusInKm = (radius*KILOMETER_MILE);
            double kmInLongitudeDegree = 111.320 * Math.cos( lat / 180.0 * Math.PI);
            double deltaLat = radiusInKm / 111.1;
            double deltaLong = radiusInKm / kmInLongitudeDegree;

            minLat = lat - deltaLat;
            maxLat = lat + deltaLat;
            minLon = lon - deltaLong;
            maxLon = lon + deltaLong;
        }

    @Override
    public String toString() {
        return "MinMaxLatLon{" +
                "minLat=" + minLat +
                ", maxLat=" + maxLat +
                ", minLon=" + minLon +
                ", maxLon=" + maxLon +
                '}';
    }
}

