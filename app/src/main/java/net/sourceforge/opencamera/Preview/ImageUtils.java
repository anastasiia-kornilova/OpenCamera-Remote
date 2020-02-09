package net.sourceforge.opencamera.Preview;

import android.graphics.ImageFormat;

import net.sourceforge.opencamera.CameraController.ExtractedImage;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImageUtils {

    private final int mWidth;
    private final int mHeight;
    private final byte[] mData;
    private final byte[] mRowData;

    public ImageUtils(ExtractedImage image) {
        mWidth = image.getWidth();
        mHeight = image.getHeight();
        mData = new byte[mWidth * mHeight * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8];
        mRowData = new byte[image.getRowStride(0)];
    }

    public Mat imageToMat(ExtractedImage image) {
        byte[] buffer;
        int rowStride;
        int pixelStride;
        int offset = 0;

        for (int i = 0; i < 3; i++) {
            rowStride = image.getRowStride(i);
            pixelStride = image.getPixelStride(i);
            buffer = image.getPlane(i);

            int w = (i == 0) ? mWidth : mWidth / 2;
            int h = (i == 0) ? mHeight : mHeight / 2;

            int bytesPerPixel = ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8;

            // TODO: Hardcoded. Check rowStride == length. Needed for performance
            if (pixelStride == bytesPerPixel) {
                int length = w * h * bytesPerPixel;
                System.arraycopy(buffer, 0, mData, 0, length);
                offset += length;
            } else {
                int buffOffset = 0;
                for (int row = 0; row < h; row++) {
                    if (h - row == 1) {
                        System.arraycopy(buffer, buffOffset, mRowData, 0, mWidth - pixelStride + 1);
                    } else {
                        System.arraycopy(buffer, buffOffset, mRowData, 0, rowStride);
                    }

                    for (int col = 0; col < w; col++) {
                        mData[offset++] = mRowData[col * pixelStride];
                    }
                    buffOffset += rowStride;
                }
            }
        }

        Mat mat = new Mat(mHeight + mHeight / 2, mWidth, CvType.CV_8UC1);
        mat.put(0, 0, mData);
        return mat;
    }
}
