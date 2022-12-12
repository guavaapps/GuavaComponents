package com.guavaapps.components.color;

import static java.lang.Math.max;

class Cam16 {
    static final float[][] XYZ_TO_CAM16RGB = {
            {0.401288f, 0.650173f, -0.051461f},
            {-0.250268f, 1.204414f, 0.045854f},
            {-0.002079f, 0.048952f, 0.953127f}
    };

    static final float[][] CAM16RGB_TO_XYZ = {
            {1.8620678f, -1.0112547f, 0.14918678f},
            {0.38752654f, 0.62144744f, -0.00897398f},
            {-0.01584150f, -0.03412294f, 1.0499644f}
    };

    private final float hue;
    private final float chroma;
    private final float j;
    private final float q;
    private final float m;
    private final float s;

    private final float jstar;
    private final float astar;
    private final float bstar;

    float distance(Cam16 other) {
        float dJ = getJStar() - other.getJStar();
        float dA = getAStar() - other.getAStar();
        float dB = getBStar() - other.getBStar();
        double dEPrime = Math.sqrt(dJ * dJ + dA * dA + dB * dB);
        double dE = 1.41 * Math.pow(dEPrime, 0.63);
        return (float) dE;
    }

    public float getHue() {
        return hue;
    }

    public float getChroma() {
        return chroma;
    }

    public float getJ() {
        return j;
    }

    public float getQ() {
        return q;
    }

    public float getM() {
        return m;
    }

    public float getS() {
        return s;
    }

    public float getJStar() {
        return jstar;
    }

    public float getAStar() {
        return astar;
    }

    public float getBStar() {
        return bstar;
    }

    private Cam16(
            float hue,
            float chroma,
            float j,
            float q,
            float m,
            float s,
            float jstar,
            float astar,
            float bstar) {
        this.hue = hue;
        this.chroma = chroma;
        this.j = j;
        this.q = q;
        this.m = m;
        this.s = s;
        this.jstar = jstar;
        this.astar = astar;
        this.bstar = bstar;
    }


    public static Cam16 fromInt(int argb) {
        return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT);
    }

    @SuppressWarnings("FloatingPointLiteralPrecision")
    static Cam16 fromIntInViewingConditions(int argb, ViewingConditions viewingConditions) {

        int red = (argb & 0x00ff0000) >> 16;
        int green = (argb & 0x0000ff00) >> 8;
        int blue = (argb & 0x000000ff);
        float redL = ColorUtils.linearized(red / 255f) * 100f;
        float greenL = ColorUtils.linearized(green / 255f) * 100f;
        float blueL = ColorUtils.linearized(blue / 255f) * 100f;
        float x = 0.41233895f * redL + 0.35762064f * greenL + 0.18051042f * blueL;
        float y = 0.2126f * redL + 0.7152f * greenL + 0.0722f * blueL;
        float z = 0.01932141f * redL + 0.11916382f * greenL + 0.95034478f * blueL;


        float[][] matrix = XYZ_TO_CAM16RGB;
        float rT = (x * matrix[0][0]) + (y * matrix[0][1]) + (z * matrix[0][2]);
        float gT = (x * matrix[1][0]) + (y * matrix[1][1]) + (z * matrix[1][2]);
        float bT = (x * matrix[2][0]) + (y * matrix[2][1]) + (z * matrix[2][2]);


        float rD = viewingConditions.getRgbD()[0] * rT;
        float gD = viewingConditions.getRgbD()[1] * gT;
        float bD = viewingConditions.getRgbD()[2] * bT;


        float rAF = (float) Math.pow(viewingConditions.getFl() * Math.abs(rD) / 100.0, 0.42);
        float gAF = (float) Math.pow(viewingConditions.getFl() * Math.abs(gD) / 100.0, 0.42);
        float bAF = (float) Math.pow(viewingConditions.getFl() * Math.abs(bD) / 100.0, 0.42);
        float rA = Math.signum(rD) * 400.0f * rAF / (rAF + 27.13f);
        float gA = Math.signum(gD) * 400.0f * gAF / (gAF + 27.13f);
        float bA = Math.signum(bD) * 400.0f * bAF / (bAF + 27.13f);


        float a = (float) (11.0 * rA + -12.0 * gA + bA) / 11.0f;

        float b = (float) (rA + gA - 2.0 * bA) / 9.0f;


        float u = (20.0f * rA + 20.0f * gA + 21.0f * bA) / 20.0f;
        float p2 = (40.0f * rA + 20.0f * gA + bA) / 20.0f;


        float atan2 = (float) Math.atan2(b, a);
        float atanDegrees = atan2 * 180.0f / (float) Math.PI;
        float hue =
                atanDegrees < 0
                        ? atanDegrees + 360.0f
                        : atanDegrees >= 360 ? atanDegrees - 360.0f : atanDegrees;
        float hueRadians = hue * (float) Math.PI / 180.0f;


        float ac = p2 * viewingConditions.getNbb();


        float j =
                100.0f
                        * (float)
                        Math.pow(
                                ac / viewingConditions.getAw(),
                                viewingConditions.getC() * viewingConditions.getZ());
        float q =
                4.0f
                        / viewingConditions.getC()
                        * (float) Math.sqrt(j / 100.0f)
                        * (viewingConditions.getAw() + 4.0f)
                        * viewingConditions.getFlRoot();


        float huePrime = (hue < 20.14) ? hue + 360 : hue;
        float eHue = 0.25f * (float) (Math.cos(Math.toRadians(huePrime) + 2.0) + 3.8);
        float p1 = 50000.0f / 13.0f * eHue * viewingConditions.getNc() * viewingConditions.getNcb();
        float t = p1 * (float) Math.hypot(a, b) / (u + 0.305f);
        float alpha =
                (float) Math.pow(1.64 - Math.pow(0.29, viewingConditions.getN()), 0.73)
                        * (float) Math.pow(t, 0.9);

        float c = alpha * (float) Math.sqrt(j / 100.0);
        float m = c * viewingConditions.getFlRoot();
        float s =
                50.0f
                        * (float)
                        Math.sqrt((alpha * viewingConditions.getC()) / (viewingConditions.getAw() + 4.0f));


        float jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j);
        float mstar = 1.0f / 0.0228f * (float) Math.log1p(0.0228f * m);
        float astar = mstar * (float) Math.cos(hueRadians);
        float bstar = mstar * (float) Math.sin(hueRadians);

        return new Cam16(hue, c, j, q, m, s, jstar, astar, bstar);
    }


    static Cam16 fromJch(float j, float c, float h) {
        return fromJchInViewingConditions(j, c, h, ViewingConditions.DEFAULT);
    }


    private static Cam16 fromJchInViewingConditions(
            float j, float c, float h, ViewingConditions viewingConditions) {
        float q =
                4.0f
                        / viewingConditions.getC()
                        * (float) Math.sqrt(j / 100.0)
                        * (viewingConditions.getAw() + 4.0f)
                        * viewingConditions.getFlRoot();
        float m = c * viewingConditions.getFlRoot();
        float alpha = c / (float) Math.sqrt(j / 100.0);
        float s =
                50.0f
                        * (float)
                        Math.sqrt((alpha * viewingConditions.getC()) / (viewingConditions.getAw() + 4.0f));

        float hueRadians = h * (float) Math.PI / 180.0f;
        float jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j);
        float mstar = 1.0f / 0.0228f * (float) Math.log1p(0.0228 * m);
        float astar = mstar * (float) Math.cos(hueRadians);
        float bstar = mstar * (float) Math.sin(hueRadians);
        return new Cam16(h, c, j, q, m, s, jstar, astar, bstar);
    }


    public static Cam16 fromUcs(float jstar, float astar, float bstar) {

        return fromUcsInViewingConditions(jstar, astar, bstar, ViewingConditions.DEFAULT);
    }


    public static Cam16 fromUcsInViewingConditions(
            float jstar, float astar, float bstar, ViewingConditions viewingConditions) {

        double m = Math.hypot(astar, bstar);
        double m2 = Math.expm1(m * 0.0228f) / 0.0228f;
        double c = m2 / viewingConditions.getFlRoot();
        double h = Math.atan2(bstar, astar) * (180.0f / Math.PI);
        if (h < 0.0) {
            h += 360.0f;
        }
        float j = jstar / (1f - (jstar - 100f) * 0.007f);
        return fromJchInViewingConditions(j, (float) c, (float) h, viewingConditions);
    }


    public int getInt() {
        return viewed(ViewingConditions.DEFAULT);
    }


    int viewed(ViewingConditions viewingConditions) {
        float alpha =
                (getChroma() == 0.0 || getJ() == 0.0)
                        ? 0.0f
                        : getChroma() / (float) Math.sqrt(getJ() / 100.0);

        float t =
                (float)
                        Math.pow(
                                alpha / Math.pow(1.64 - Math.pow(0.29, viewingConditions.getN()), 0.73), 1.0 / 0.9);
        float hRad = getHue() * (float) Math.PI / 180.0f;

        float eHue = 0.25f * (float) (Math.cos(hRad + 2.0) + 3.8);
        float ac =
                viewingConditions.getAw()
                        * (float)
                        Math.pow(getJ() / 100.0, 1.0 / viewingConditions.getC() / viewingConditions.getZ());
        float p1 = eHue * (50000.0f / 13.0f) * viewingConditions.getNc() * viewingConditions.getNcb();
        float p2 = (ac / viewingConditions.getNbb());

        float hSin = (float) Math.sin(hRad);
        float hCos = (float) Math.cos(hRad);

        float gamma = 23.0f * (p2 + 0.305f) * t / (23.0f * p1 + 11.0f * t * hCos + 108.0f * t * hSin);
        float a = gamma * hCos;
        float b = gamma * hSin;
        float rA = (460.0f * p2 + 451.0f * a + 288.0f * b) / 1403.0f;
        float gA = (460.0f * p2 - 891.0f * a - 261.0f * b) / 1403.0f;
        float bA = (460.0f * p2 - 220.0f * a - 6300.0f * b) / 1403.0f;

        float rCBase = (float) max(0, (27.13 * Math.abs(rA)) / (400.0 - Math.abs(rA)));
        float rC =
                Math.signum(rA)
                        * (100.0f / viewingConditions.getFl())
                        * (float) Math.pow(rCBase, 1.0 / 0.42);
        float gCBase = (float) max(0, (27.13 * Math.abs(gA)) / (400.0 - Math.abs(gA)));
        float gC =
                Math.signum(gA)
                        * (100.0f / viewingConditions.getFl())
                        * (float) Math.pow(gCBase, 1.0 / 0.42);
        float bCBase = (float) max(0, (27.13 * Math.abs(bA)) / (400.0 - Math.abs(bA)));
        float bC =
                Math.signum(bA)
                        * (100.0f / viewingConditions.getFl())
                        * (float) Math.pow(bCBase, 1.0 / 0.42);
        float rF = rC / viewingConditions.getRgbD()[0];
        float gF = gC / viewingConditions.getRgbD()[1];
        float bF = bC / viewingConditions.getRgbD()[2];

        float[][] matrix = CAM16RGB_TO_XYZ;
        float x = (rF * matrix[0][0]) + (gF * matrix[0][1]) + (bF * matrix[0][2]);
        float y = (rF * matrix[1][0]) + (gF * matrix[1][1]) + (bF * matrix[1][2]);
        float z = (rF * matrix[2][0]) + (gF * matrix[2][1]) + (bF * matrix[2][2]);

        return ColorUtils.intFromXyzComponents(x, y, z);
    }
}