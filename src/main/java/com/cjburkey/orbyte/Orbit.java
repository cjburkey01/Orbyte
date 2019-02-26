package com.cjburkey.orbyte;

import static java.lang.Math.*;

/**
 * Created by CJ Burkey on 2019/02/22
 * <br/>
 * Source: http://www.aerospacengineering.net/?p=521
 * <br/>
 * Please not that I am writing this out of passion and am neither an orbital physicist nor do I have any special knowledge.
 * This is the culmination of hours of research on multiple different websites and I'm sure my ignorance shows.
 */
public final class Orbit {

    private static final double G = 6.67408E-11;

    // -- BEGIN PURE FUNCTIONS -- //

    /**
     * @param eccentricity <code>e</code> The eccentricity of the orbit
     * @param trueAnomoly  <code>v</code> The radians since the periapsis
     * @return The eccentric anomoly <code>E</code>; the angular position of the orbit relative to the major axis in radians.
     */
    public static double getEccentricAnomoly(final double eccentricity,
                                             final double trueAnomoly) {
        // 4.40
        return acos((eccentricity + cos(trueAnomoly)) / (1.0d + eccentricity * cos(trueAnomoly)));
    }

    /**
     * @param eccentricAnomoly <code>E</code> The angular position of the orbit relative to the major axis in radians.
     * @param eccentricity     <code>e</code> The eccentricity of the orbit.
     * @return The mean anomoly of the orbit at the given eccentric anomoly <code>M</code>; the fraction of the period that has elapsed since the orbiting body passed periapsis.
     */
    public static double getMeanAnomoly(final double eccentricAnomoly,
                                        final double eccentricity) {
        // 4.41
        return eccentricAnomoly - eccentricity * sin(eccentricAnomoly);
    }

    /**
     * @param parentMass          <code>M<sub>p</sub></code> The mass of the reference object for the orbit in meters.
     * @param semiMajorAxisMeters <code>a</code> The length of the semi-major axis of the orbit in meters.
     * @return <code>n</code> The average angular velocity along the orbit.
     */
    public static double getAverageAngularVelocity(final double parentMass,
                                                   final double semiMajorAxisMeters) {
        // 4.39
        return sqrt((G * parentMass) / (semiMajorAxisMeters * semiMajorAxisMeters * semiMajorAxisMeters));
    }

    /**
     * @param meanAnomolyStart       <code>M<sub>0</sub></code> The starting mean anomoly.
     * @param meanAnomolyFinish      <code>M</code> The final mean anomoly.
     * @param averageAngularVelocity <code>n</code> The average angular velocity of the orbit.
     * @return <code>t</code> The time elapsed between the starting mean anomoly and the ending mean anomoly in seconds.
     */
    private static double getTimeElapsed(final double meanAnomolyStart,
                                         final double meanAnomolyFinish,
                                         final double averageAngularVelocity) {
        // 4.38
        return (meanAnomolyFinish - meanAnomolyStart) / averageAngularVelocity;
    }

    /**
     * @param eccentricAnomoly <code>E</code> The angular position of the orbit relative to the major axis in radians.
     * @param eccentricity     <code>e</code> The eccentricity of the orbit.
     * @return The true anomoly of the orbit at the orbiting body's position <code>v</code>; the angle between the direction of periapsis and the current position of the orbiting body in radians.
     */
    public static double getTrueAnomoly(final double eccentricAnomoly,
                                        final double eccentricity) {
        // 4.40
        return acos((cos(eccentricAnomoly) - eccentricity) / (1.0d - eccentricity * cos(eccentricAnomoly)));
    }

    /**
     * @param meanAnomoly          <code>M</code> The fraction of the period that has elapsed since the orbiting body passed periapsis.
     * @param eccentricity         <code>e</code> The eccentricity of the orbit.
     * @param maxIterations        The maximum number of iterations that will be run to approximate the value.
     * @param decimalPlaceAccuracy The number of decimal places to which the answer should be accurate.
     * @return The eccentric anomoly <code>E</code>; the angular position of the orbit relative to the major axis in radians.
     */
    public static double getEccentricAnomoly(final double meanAnomoly,
                                             final double eccentricity,
                                             final int maxIterations,
                                             final int decimalPlaceAccuracy) {
        // From: http://www.jgiesen.de/kepler/kepler.html
        // Ported to Java from Javascript by me. You're welcome, me.
        double delta = pow(10.0d, -decimalPlaceAccuracy);
        double m = (toDegrees(meanAnomoly) / 360.0d);
        m = (2.0d * PI * (m - floor(m)));

        double E = ((eccentricity < 0.8d) ? m : PI);
        double F = (E - (eccentricity * sin(m)) - m);

        int i = 0;
        while ((abs(F) > delta) && (i < maxIterations)) {
            E = (E - (F / (1.0d - eccentricity * cos(E))));
            F = (E - (eccentricity * sin(E)) - m);
            i++;
        }

        return round(E * pow(10, decimalPlaceAccuracy)) / pow(10, decimalPlaceAccuracy);
    }

}
