package net.sourceforge.opencamera.CameraController;

import android.media.Image;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ExtractedImage {

    private static final int mPlaneCount = 3;
    private int[] mRowStrides = new int[mPlaneCount];
    private int[] mPixelStrides = new int[mPlaneCount];
    private int mWidth;
    private int mHeight;
    private List<byte[]> mPlanes;
    private long mTimestamp;

    public ExtractedImage(Image image, long ts) {
        mWidth = image.getWidth();
        mHeight = image.getHeight();
        mTimestamp = ts;
        mPlanes = new ArrayList<>(mPlaneCount);
        for (int i = 0; i < mPlaneCount; i++) {
            Image.Plane plane = image.getPlanes()[i];
            mRowStrides[i] = plane.getRowStride();
            mPixelStrides[i] = plane.getPixelStride();

            ByteBuffer byteBuffer = plane.getBuffer();
            int length = byteBuffer.remaining();
            byte[] bytes = new byte[length];
            byteBuffer.get(bytes, 0, length);
            mPlanes.add(bytes);
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getRowStride(int index) {
        return mRowStrides[index];
    }

    public int getPixelStride(int index) {
        return mPixelStrides[index];
    }

    public byte[] getPlane(int index) {
        return mPlanes.get(index);
    }

    public long getFrameTimestamp() {
        return mTimestamp;
    }
}