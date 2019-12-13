package net.zhongbenshuo.attendance.FaceEngine;

import com.arcsoft.face.Rect;

public class ImageUtils {
    private static final int FACE_DETECT_IMAGE_WIDTH_LIMIT = 4;
    private static final int FACE_DETECT_IMAGE_HEIGHT_LIMIT = 2;
    private static final int VALUE_FOR_4_ALIGN = 3;
    private static final int VALUE_FOR_2_ALIGN = 1;

    public ImageUtils() {
    }
//    public static Bitmap bgrToBitmap(byte[] bgr, int width, int height) {
//        int[] colors = convertBgrToColor(bgr);
//        return colors == null ? null : Bitmap.createBitmap(colors, 0, width, width, height, Config.ARGB_8888);
//    }
    private static int[] convertBgrToColor(byte[] bgr) {
        int size = bgr.length;
        if (size == 0) {
            return null;
        } else if (size % 3 != 0) {
            return null;
        } else {
            int[] color = new int[size / 3];
            int index = 0;

            for(int i = 0; i < color.length; ++i) {
                color[i] = (bgr[index + 2] & 255) << 16 | (bgr[index + 1] & 255) << 8 | bgr[index] & 255 | -16777216;
                index += 3;
            }

            return color;
        }
    }
    public static byte[] cropBgr24(byte[] bgr24, int width, int height, Rect rect) {
        if (bgr24 != null && bgr24.length != 0 && width * height * 3 == bgr24.length && rect != null) {
            if (rect.left >= 0 && rect.top >= 0 && rect.right <= width && rect.bottom <= height) {
                if (rect.right > rect.left && rect.bottom > rect.top) {
                    int cropImageWidth = rect.right - rect.left;
                    int cropImageHeight = rect.bottom - rect.top;
                    byte[] cropBgr24 = new byte[cropImageWidth * cropImageHeight * 3];
                    int originalLineStart = rect.top * width * 3;
                    int targetIndex = 0;

                    for(int i = rect.top; i < rect.bottom; ++i) {
                        System.arraycopy(bgr24, originalLineStart + rect.left * 3, cropBgr24, targetIndex, cropImageWidth * 3);
                        originalLineStart += width * 3;
                        targetIndex += cropImageWidth * 3;
                    }

                    return cropBgr24;
                } else {
                    throw new IllegalArgumentException("invalid rect!");
                }
            } else {
                throw new IllegalArgumentException("rect out of bounds!");
            }
        } else {
            throw new IllegalArgumentException("invalid image params!");
        }
    }
}