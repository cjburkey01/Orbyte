package com.cjburkey.orbyte;

import java.util.Objects;

import static java.lang.Math.*;

/**
 * Created by CJ Burkey on 2019/02/22
 * <br/>
 * Source: http://www.aerospacengineering.net/?p=521
 * <br/>
 * Please not that I am writing this out of passion and am neither an orbital physicist nor do I have any special knowledge.
 * This is the culmination of hours of research on multiple different websites and I'm sure my ignorance shows.
 */
@SuppressWarnings("WeakerAccess")
public final class Orbit {

    private static final double G = 6.67408E-11;

    // Given
    private final double semiMajorAxis;
    private final double eccentricity;
    private final double inclination;
    private final double argumentOfPeriapsis;
    private final double timeOfPeriapsisPassage;
    private final double longitudeOfAscendingNode;
    private final double parentMass;
    private final double mass;

    // Calculated
    private final double orbitalPeriod;
    private final double meanMotion;

    private Orbit(double semiMajorAxis,
                  double eccentricity,
                  double inclination,
                  double argumentOfPeriapsis,
                  double timeOfPeriapsisPassage,
                  double longitudeOfAscendingNode,
                  double parentMass,
                  double mass) {
        this.semiMajorAxis = semiMajorAxis;
        this.eccentricity = eccentricity;
        this.inclination = inclination;
        this.argumentOfPeriapsis = argumentOfPeriapsis;
        this.timeOfPeriapsisPassage = timeOfPeriapsisPassage;
        this.longitudeOfAscendingNode = longitudeOfAscendingNode;
        this.parentMass = parentMass;
        this.mass = mass;

        orbitalPeriod = sqrt((4.0d * (PI * PI) * (semiMajorAxis * semiMajorAxis * semiMajorAxis)) / (G * (parentMass + mass)));
        meanMotion = sqrt((G * parentMass) / (semiMajorAxis * semiMajorAxis * semiMajorAxis));
    }

    private double getMeanAnomolyChange(double startTime, double endTime) {
        return meanMotion * (endTime - startTime);
    }

    private double getVelocity(double radius) {
        return sqrt((G * parentMass) * ((2.0d / radius) - (1.0d / semiMajorAxis)));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orbit orbit = (Orbit) o;
        return Double.compare(orbit.semiMajorAxis, semiMajorAxis) == 0 &&
                Double.compare(orbit.eccentricity, eccentricity) == 0 &&
                Double.compare(orbit.inclination, inclination) == 0 &&
                Double.compare(orbit.argumentOfPeriapsis, argumentOfPeriapsis) == 0 &&
                Double.compare(orbit.timeOfPeriapsisPassage, timeOfPeriapsisPassage) == 0 &&
                Double.compare(orbit.longitudeOfAscendingNode, longitudeOfAscendingNode) == 0 &&
                Double.compare(orbit.parentMass, parentMass) == 0 &&
                Double.compare(orbit.mass, mass) == 0;
    }

    public int hashCode() {
        return Objects.hash(semiMajorAxis,
                eccentricity,
                inclination,
                argumentOfPeriapsis,
                timeOfPeriapsisPassage,
                longitudeOfAscendingNode,
                parentMass,
                mass);
    }

    // -- BEGIN PURE FUNCTIONS -- //

    /**
     * @param eccentricity         The eccentricity of the orbit
     * @param radiansPastPeriapsis The radians since the periapsis
     * @return The eccentric anomoly; the angular position of the orbit relative to the major axis in radians.
     */
    public static double getEccentricAnomoly(double eccentricity,
                                             double radiansPastPeriapsis) {
        // 4.40
        return acos((eccentricity + cos(radiansPastPeriapsis)) / (1.0d + eccentricity * cos(radiansPastPeriapsis)));
    }

    /**
     * @param eccentricAnomoly The angular position of the orbit relative to the major axis in radians.
     * @param eccentricity     The eccentricity of the orbit.
     * @return The mean anomoly of the orbit at the given eccentric anomoly; the fraction of the period that has elapsed since the orbiting body passed periapsis.
     */
    public static double getMeanAnomoly(double eccentricAnomoly,
                                        double eccentricity) {
        // 4.41
        return eccentricAnomoly - eccentricity * sin(eccentricAnomoly);
    }

    public static double getAverageAngularVelocity(double parentMass,
                                                   double semiMajorAxisMeters) {
        // 4.39
        return sqrt((G * parentMass) / (semiMajorAxisMeters * semiMajorAxisMeters * semiMajorAxisMeters));
    }

    private static double getTimeElapsed(double meanAnomolyStart,
                                         double meanAnomolyFinish,
                                         double averageVelocity) {
        // 4.38
        return (meanAnomolyFinish - meanAnomolyStart) / averageVelocity;
    }

    /**
     * @param eccentricAnomoly The angular position of the orbit relative to the major axis in radians.
     * @param eccentricity     The eccentricity of the orbit.
     * @return The true anomoly of the orbit at the orbiting body's position; the angle between the direction of periapsis and the current position of the orbiting body in radians.
     */
    public static double getTrueAnomoly(double eccentricAnomoly,
                                        double eccentricity) {
        // 4.40
        return acos((cos(eccentricAnomoly) - eccentricity) / (1.0d - eccentricity * cos(eccentricAnomoly)));
    }

    private static double timeFromTo(double semiMajorAxisMeters,
                                     double eccentricity,
                                     double parentMass,
                                     double degreesPastPeriapsisBefore,
                                     double degreesPastPeriapsisAfter) {
        final double v0 = degreesPastPeriapsisBefore * PI / 180.0d;
        final double v = degreesPastPeriapsisAfter * PI / 180.0d;

        final double E0 = getEccentricAnomoly(eccentricity, v0);
        final double E = getEccentricAnomoly(eccentricity, v);

        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double M = getMeanAnomoly(E, eccentricity);

        final double n = getAverageAngularVelocity(parentMass, semiMajorAxisMeters);

        return getTimeElapsed(M0, M, n);
    }

    private static double getTrueAnomolyAfter(double semiMajorAxisMeters,
                                              double eccentricity,
                                              double parentMass,
                                              double degreesPastPeriapsisBefore,
                                              double time,
                                              double iterationAccuracy) {
        final double v0 = degreesPastPeriapsisBefore * PI / 180.0d;

        final double E0 = getEccentricAnomoly(eccentricity, v0);

        final double M0 = getMeanAnomoly(E0, eccentricity);

        final double n = getAverageAngularVelocity(parentMass, semiMajorAxisMeters);

        // 4.38
        final double M = M0 + n * time;

        final double E = getIterateEccentricAnomoly(M, eccentricity, iterationAccuracy);

        return getTrueAnomoly(E, eccentricity);
    }

    private static double getIterateEccentricAnomoly(double M,
                                                     double e,
                                                     double accuracy) {
        // 4.41
        double Enext = 0.0d;
        double Eprev = 0.0d;
        do {
            double tmpNextPrev = Enext;
            Enext = Eprev + ((M + e * sin(Eprev) - Eprev) / (1.0d - e * cos(Eprev)));
            Eprev = tmpNextPrev;
        } while (abs(Enext - Eprev) > accuracy);
        return Enext;
    }

}
