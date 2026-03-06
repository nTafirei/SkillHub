package com.marotech.skillhub.components.service;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {
    public class GeoDistanceCalculator {

        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);

            double a = Math.pow(Math.sin(dLat / 2), 2) +
                    Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS_KM * c;
        }

        private static final double EARTH_RADIUS_KM = 6371.0;
    }
}
