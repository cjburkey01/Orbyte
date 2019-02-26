package com.cjburkey.orbyte;

import org.junit.jupiter.api.Test;

import static com.cjburkey.orbyte.Orbit.*;
import static java.lang.Math.*;

/**
 * Created by CJ Burkey on 2019/02/26
 */
@SuppressWarnings("unused")
public class Tests {

    @Test
    void testProblem413_01() {
        assertTestProblem413(0.1d,
                30.0d,
                90.0d,
                5.972E24d,
                7.5E6d,
                0.47557d,
                1.47063d,
                0.42978d,
                1.37113d,
                0.00097202d,
                968.4694d);
    }

    @Test
    void testProblem414_01() {
        assertTestProblem414(0.1d,
                90.0d,
                5.972E24d,
                7_500_000.0d,
                1_200.0d,
                30,
                10,
                1.37113d,
                0.00097202d,
                2.53755d,
                2.58996d,
                2.64034d);
    }

    // -- PROBLEMS -- //
    // These problems refer to: http://www.aerospacengineering.net/?p=521

    private static void assertTestProblem413(final double eccentricity,
                                             final double startTrueAnomolyDegrees,
                                             final double endTrueAnomolyDegrees,
                                             final double parentMass,
                                             final double semiMajorAxis,
                                             final double expE0,
                                             final double expE,
                                             final double expM0,
                                             final double expM,
                                             final double expn,
                                             final double expt) {
        final double E0 = getEccentricAnomoly(eccentricity, toRadians(startTrueAnomolyDegrees));
        final double E = getEccentricAnomoly(eccentricity, toRadians(endTrueAnomolyDegrees));
        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double M = getMeanAnomoly(E, eccentricity);
        final double n = getAverageAngularVelocity(parentMass, semiMajorAxis);
        final double t = (M - M0) / n;

        assertAccurate(E0, expE0);
        assertAccurate(E, expE);
        assertAccurate(M0, expM0);
        assertAccurate(M, expM);
        assertAccurate(n, expn);
        assertAccurate(t, expt);
    }

    private static void assertTestProblem414(final double eccentricity,
                                             final double startTrueAnomolyDegrees,
                                             final double parentMass,
                                             final double semiMajorAxis,
                                             final double time,
                                             final int maxIterations,
                                             final int decimalPlaceAccuracy,
                                             final double expM0,
                                             final double expn,
                                             final double expM,
                                             final double expE,
                                             final double expv) {
        final double E0 = getEccentricAnomoly(eccentricity, toRadians(startTrueAnomolyDegrees));
        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double n = getAverageAngularVelocity(parentMass, semiMajorAxis);
        final double M = M0 + n * time;
        final double E = getEccentricAnomoly(M, eccentricity, maxIterations, decimalPlaceAccuracy);
        final double v = acos((cos(E) - eccentricity) / (1.0d - eccentricity * cos(E)));

        assertAccurate(M0, expM0);
        assertAccurate(n, expn);
        assertAccurate(M, expM);
        assertAccurate(E, expE);
        assertAccurate(v, expv);
    }

    // -- UTILS -- //

    private static void assertAccurate(double a, double b, @SuppressWarnings("SameParameterValue") double accuracy) {
        assert (abs(a - b) < accuracy);
    }

    private static void assertAccurate(double a, double b) {
        assertAccurate(a, b, 1.0E-4d);
    }

}
