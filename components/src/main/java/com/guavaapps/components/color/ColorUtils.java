package com.guavaapps.components.color;

import java.util.Arrays;

class ColorUtils {
        private ColorUtils() {}

        private static final float[] WHITE_POINT_D65 = {95.047f, 100.0f, 108.883f};

        
        public static final float[] whitePointD65() {
            return Arrays.copyOf(WHITE_POINT_D65, 3);
        }

        
        public static int redFromInt(int argb) {
            return (argb & 0x00ff0000) >> 16;
        }

        
        public static int greenFromInt(int argb) {
            return (argb & 0x0000ff00) >> 8;
        }

        
        public static int blueFromInt(int argb) {
            return (argb & 0x000000ff);
        }

        
        public static float lstarFromInt(int argb) {
            return (float) labFromInt(argb)[0];
        }

        
        public static String hexFromInt(int argb) {
            int red = redFromInt(argb);
            int blue = blueFromInt(argb);
            int green = greenFromInt(argb);
            return String.format("#%02x%02x%02x", red, green, blue);
        }

        
        
        
        
        @SuppressWarnings("FloatingPointLiteralPrecision")
        public static float[] xyzFromInt(int argb) {
            final float r = linearized(redFromInt(argb) / 255f) * 100f;
            final float g = linearized(greenFromInt(argb) / 255f) * 100f;
            final float b = linearized(blueFromInt(argb) / 255f) * 100f;
            final float x = 0.41233894f * r + 0.35762064f * g + 0.18051042f * b;
            final float y = 0.2126f * r + 0.7152f * g + 0.0722f * b;
            final float z = 0.01932141f * r + 0.11916382f * g + 0.95034478f * b;
            return new float[] {x, y, z};
        }

        
        public static int intFromRgb(int r, int g, int b) {
            return (((255 << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff)) >>> 0);
        }

        
        public static double[] labFromInt(int argb) {
            final double e = 216.0 / 24389.0;
            final double kappa = 24389.0 / 27.0;

            final float[] xyz = xyzFromInt(argb);
            final double yNormalized = xyz[1] / WHITE_POINT_D65[1];
            double fy;
            if (yNormalized > e) {
                fy = Math.cbrt(yNormalized);
            } else {
                fy = (kappa * yNormalized + 16) / 116;
            }

            final double xNormalized = xyz[0] / WHITE_POINT_D65[0];
            double fx;
            if (xNormalized > e) {
                fx = Math.cbrt(xNormalized);
            } else {
                fx = (kappa * xNormalized + 16) / 116;
            }

            final double zNormalized = xyz[2] / WHITE_POINT_D65[2];
            double fz;
            if (zNormalized > e) {
                fz = Math.cbrt(zNormalized);
            } else {
                fz = (kappa * zNormalized + 16) / 116;
            }

            final double l = 116.0 * fy - 16;
            final double a = 500.0 * (fx - fy);
            final double b = 200.0 * (fy - fz);
            return new double[] {l, a, b};
        }

        
        public static int intFromLab(double l, double a, double b) {
            final double e = 216.0 / 24389.0;
            final double kappa = 24389.0 / 27.0;
            final double ke = 8.0;

            final double fy = (l + 16.0) / 116.0;
            final double fx = (a / 500.0) + fy;
            final double fz = fy - (b / 200.0);
            final double fx3 = fx * fx * fx;
            final double xNormalized = (fx3 > e) ? fx3 : (116.0 * fx - 16.0) / kappa;
            final double yNormalized = (l > ke) ? fy * fy * fy : (l / kappa);
            final double fz3 = fz * fz * fz;
            final double zNormalized = (fz3 > e) ? fz3 : (116.0 * fz - 16.0) / kappa;
            final double x = xNormalized * WHITE_POINT_D65[0];
            final double y = yNormalized * WHITE_POINT_D65[1];
            final double z = zNormalized * WHITE_POINT_D65[2];
            return intFromXyzComponents((float) x, (float) y, (float) z);
        }

        
        public static int intFromXyzComponents(float x, float y, float z) {
            x = x / 100f;
            y = y / 100f;
            z = z / 100f;

            float rL = x * 3.2406f + y * -1.5372f + z * -0.4986f;
            float gL = x * -0.9689f + y * 1.8758f + z * 0.0415f;
            float bL = x * 0.0557f + y * -0.204f + z * 1.057f;

            float r = ColorUtils.delinearized(rL);
            float g = ColorUtils.delinearized(gL);
            float b = ColorUtils.delinearized(bL);

            int rInt = (Math.max(Math.min(255, Math.round(r * 255)), 0));
            int gInt = (Math.max(Math.min(255, Math.round(g * 255)), 0));
            int bInt = (Math.max(Math.min(255, Math.round(b * 255)), 0));
            return intFromRgb(rInt, gInt, bInt);
        }

        
        public static int intFromXyz(float[] xyz) {
            return intFromXyzComponents(xyz[0], xyz[1], xyz[2]);
        }

        
        public static int intFromLstar(float lstar) {
            float fy = (lstar + 16.0f) / 116.0f;
            float fz = fy;
            float fx = fy;

            float kappa = 24389f / 27f;
            float epsilon = 216f / 24389f;
            boolean cubeExceedEpsilon = (fy * fy * fy) > epsilon;
            boolean lExceedsEpsilonKappa = (lstar > 8.0f);
            float y = lExceedsEpsilonKappa ? fy * fy * fy : lstar / kappa;
            float x = cubeExceedEpsilon ? fx * fx * fx : (116f * fx - 16f) / kappa;
            float z = cubeExceedEpsilon ? fz * fz * fz : (116f * fx - 16f) / kappa;
            float[] xyz =
                    new float[] {
                            x * ColorUtils.WHITE_POINT_D65[0],
                            y * ColorUtils.WHITE_POINT_D65[1],
                            z * ColorUtils.WHITE_POINT_D65[2],
                    };
            return intFromXyz(xyz);
        }

        
        public static float yFromLstar(float lstar) {
            float ke = 8.0f;
            if (lstar > ke) {
                return (float) Math.pow(((lstar + 16.0) / 116.0), 3) * 100f;
            } else {
                return lstar / (24389f / 27f) * 100f;
            }
        }

        
        public static float linearized(float rgb) {
            if (rgb <= 0.04045f) {
                return (rgb / 12.92f);
            } else {
                return (float) Math.pow(((rgb + 0.055f) / 1.055f), 2.4f);
            }
        }

        
        public static float delinearized(float rgb) {
            if (rgb <= 0.0031308f) {
                return (rgb * 12.92f);
            } else {
                return ((1.055f * (float) Math.pow(rgb, 1.0f / 2.4f)) - 0.055f);
            }
        }
    }