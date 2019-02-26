package com.cjburkey.orbyte;

import static com.cjburkey.orbyte.Orbit.*;
import static java.lang.Math.*;

/**
 * Created by CJ Burkey on 2019/02/22
 */
public final class Orbyte {

    public static void main(String[] args) {
        testProblem413(0.1d, 30.0d, 90.0d, 5.972E24d, 7_500_000.0d);
    }

    private static void prinfln(String format, Object... data) {
        System.out.printf(format + '\n', data);
    }

    private static void testProblem413(final double eccentricity,
                                       final double degStart,
                                       final double degEnd,
                                       final double parentMass,
                                       final double semiMajorAxis) {
        prinfln("Problem 4.13:");
        prinfln("\tInputs:");
        prinfln("\t\tEccentricity: %.2f", eccentricity);
        prinfln("\t\tStart: %.2f deg", degStart);
        prinfln("\t\tEnd: %.2f deg", degEnd);
        prinfln("\t\tParent Mass: %.4E kg", parentMass);
        prinfln("\t\tSemi-Major Axis: %.4E m", semiMajorAxis);

        final double E0 = getEccentricAnomoly(eccentricity, toRadians(degStart));
        final double E = getEccentricAnomoly(eccentricity, toRadians(degEnd));
        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double M = getMeanAnomoly(E, eccentricity);
        final double n = getAverageAngularVelocity(parentMass, semiMajorAxis);
        final double t = (M - M0) / n;

        prinfln("\tEccentric Anomolies:");
        prinfln("\t\tE0 = %.8f rad", E0);
        prinfln("\t\tE = %.8f rad", E);

        prinfln("\tMean Anomolies:");
        prinfln("\t\tM0 = %.8f rad", M0);
        prinfln("\t\tM = %.8f rad", M);

        prinfln("\tAverage Angular Velocity:");
        prinfln("\t\tn = %.8f rad/s", n);

        prinfln("\tTime from Start to End:");
        prinfln("\t\tt = %.8f s", t);
    }

}
