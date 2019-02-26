package com.cjburkey.orbyte;

import static com.cjburkey.orbyte.Orbit.*;
import static java.lang.Math.*;

/**
 * Created by CJ Burkey on 2019/02/22
 */
public final class Orbyte {

    public static void main(String[] args) {
        printfln("Whoops, launching this does nothing.");
    }

    private static void printfln(String format, Object... data) {
        System.out.printf(format + '\n', data);
    }

    private static void testProblem413(final double eccentricity,
                                       final double startTrueAnomolyDegrees,
                                       final double endTrueAnomolyDegrees,
                                       final double parentMass,
                                       final double semiMajorAxis) {
        printfln("Problem 4.13:");
        printfln("\tInputs:");
        printfln("\t\tEccentricity: %.2f", eccentricity);
        printfln("\t\tStart True Anomoly: %.2f deg", startTrueAnomolyDegrees);
        printfln("\t\tEnd True Anomoly: %.2f deg", endTrueAnomolyDegrees);
        printfln("\t\tParent Mass: %.4E kg", parentMass);
        printfln("\t\tSemi-Major Axis: %.4E m", semiMajorAxis);

        final double E0 = getEccentricAnomoly(eccentricity, toRadians(startTrueAnomolyDegrees));
        final double E = getEccentricAnomoly(eccentricity, toRadians(endTrueAnomolyDegrees));
        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double M = getMeanAnomoly(E, eccentricity);
        final double n = getAverageAngularVelocity(parentMass, semiMajorAxis);
        final double t = (M - M0) / n;

        printfln("\tEccentric Anomolies:");
        printfln("\t\tE0 = %.8f rad", E0);
        printfln("\t\tE = %.8f rad", E);

        printfln("\tMean Anomolies:");
        printfln("\t\tM0 = %.8f rad", M0);
        printfln("\t\tM = %.8f rad", M);

        printfln("\tAverage Angular Velocity:");
        printfln("\t\tn = %.8f rad/s", n);

        printfln("\tTime from Start to End:");
        printfln("\t\tt = %.8f s", t);
    }

    private static void testProblem414(final double semiMajorAxis,
                                       final double eccentricity,
                                       final double time,
                                       final double parentMass,
                                       final double startTrueAnomolyDegrees,
                                       final int maxIterations,
                                       final int decimalPlaceAccuracy) {
        printfln("Problem 4.14:");
        printfln("\tInputs:");
        printfln("\t\tSemi-Major Axis: %.4E m", semiMajorAxis);
        printfln("\t\tEccentricity: %.2f", eccentricity);
        printfln("\t\tTime: %.2f s", time);
        printfln("\t\tParent Mass: %.4E kg", parentMass);
        printfln("\t\tStart True Anomoly: %.2f deg", startTrueAnomolyDegrees);
        printfln("\t\tMax Iterations: %s", maxIterations);
        printfln("\t\tIteration Decimal Place Accuracy: %s", decimalPlaceAccuracy);

        final double E0 = getEccentricAnomoly(eccentricity, toRadians(startTrueAnomolyDegrees));
        final double M0 = getMeanAnomoly(E0, eccentricity);
        final double n = getAverageAngularVelocity(parentMass, semiMajorAxis);
        final double M = M0 + n * time;
        final double E = getEccentricAnomoly(M, eccentricity, maxIterations, decimalPlaceAccuracy);
        final double v = acos((cos(E) - eccentricity) / (1.0d - eccentricity * cos(E)));

        printfln("\tEccentric Anomoly:");
        printfln("\t\tE0 = %.8f rad", E0);
        printfln("\t\tE = %.8f rad", E);

        printfln("\tMean Anomolies:");
        printfln("\t\tM0 = %.8f rad", M0);
        printfln("\t\tM = %.8f rad", M);

        printfln("\tAverage Angular Velocity:");
        printfln("\t\tn = %.8f rad/s", n);

        printfln("\tTrue Anomoly:");
        printfln("\t\tv = %.8f rad", v);
    }

}
