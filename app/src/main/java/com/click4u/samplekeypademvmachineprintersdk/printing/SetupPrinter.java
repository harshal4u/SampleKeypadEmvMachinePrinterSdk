package com.click4u.samplekeypademvmachineprintersdk.printing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SetupPrinter {
    private static SetupPrinter printer;
    private Paint mBitPaint;
    private static final int WIDTH = 500;
    private static List<Bitmap> bitmaps;
    private final ExecutorService sigleThreadExecutor = Executors.newSingleThreadExecutor();
    private boolean isPrinting = false;
    Context context;
    static {
        bitmaps = new ArrayList();
    }

    public SetupPrinter(Context context) {
        this.context = context;
    }

    public static SetupPrinter getInstance(Context context) {
        if (bitmaps != null) {
            bitmaps.clear();
        }

        if (printer == null) {
            synchronized(SetupPrinter.class) {
                if (printer == null) {
                    printer = new SetupPrinter(context);
                }
            }
        }

        return printer;
    }

    public void setPrintAppendString(String string, PrnStrFormat format) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(-16777216);
        textPaint.setTextSize((float)format.getTextSize());
        textPaint.setUnderlineText(format.isUnderline());
        textPaint.setTextScaleX(format.getTextScaleX());
        String textFont = format.getFont().toString();
        Typeface font;
        if (textFont.equals(PrnTextFont.DEFAULT.toString())) {
            font = Typeface.create(Typeface.DEFAULT, format.getStyle().ordinal());
            textPaint.setTypeface(font);
        } else if (textFont.equals(PrnTextFont.DEFAULT_BOLD.toString())) {
            font = Typeface.create(Typeface.DEFAULT_BOLD, format.getStyle().ordinal());
            textPaint.setTypeface(font);
        } else if (textFont.equals(PrnTextFont.MONOSPACE.toString())) {
            font = Typeface.create(Typeface.MONOSPACE, format.getStyle().ordinal());
            textPaint.setTypeface(font);
        } else if (textFont.equals(PrnTextFont.SANS_SERIF.toString())) {
            font = Typeface.create(Typeface.SANS_SERIF, format.getStyle().ordinal());
            textPaint.setTypeface(font);
        } else if (textFont.equals(PrnTextFont.SERIF.toString())) {
            font = Typeface.create(Typeface.SERIF, format.getStyle().ordinal());
            textPaint.setTypeface(font);
        } else if (!HCBoolean.isEmpty(format.getPath())) {
            font = Typeface.createFromAsset(context.getAssets(),format.getPath());
            textPaint.setTypeface(font);
            if (format.getStyle() == PrnTextStyle.BOLD) {
                textPaint.setFakeBoldText(true);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(format.getLetterSpacing());
        }
        StaticLayout layout = new StaticLayout(string, textPaint, 384, format.getAli(), 1.0F, 0.0F, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight() + 2, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(1.0F, 0.0F);
        canvas.drawColor(-1);
        layout.draw(canvas);
        bitmaps.add(bitmap);
    }

//    public void setPrintAppendQRCode(String content, int width, int height, Alignment alignment) {
//        Bitmap bitmap = this.createQRImage(content, width, height, alignment);
//        if (bitmap != null) {
//            bitmaps.add(bitmap);
//        }
//
//    }

//    public void setPrintAppendBarCode(Context context, String contents, int desiredWidth, int desiredHeight, boolean displayCode, Alignment alignment, BarcodeFormat format) {
//        Bitmap bitmap = this.creatBarcode(context, contents, desiredWidth, desiredHeight, displayCode, alignment, format);
//        bitmaps.add(bitmap);
//    }

    public void setPrintAppendBitmap(Bitmap bitmap, Alignment alignment) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 384;
        int newHeight = (int)((float)height * (384.0F / (float)width));
        Bitmap bitmap1;
        if (width > newWidth) {
            float scaleWidth = (float)newWidth / (float)width;
            float scaleHeight = (float)newHeight / (float)height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } else {
            bitmap1 = bitmap;
        }

        Bitmap newbmp = Bitmap.createBitmap(384, bitmap1.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        if (alignment == Alignment.ALIGN_NORMAL) {
            cv.drawBitmap(bitmap1, 0.0F, 0.0F, (Paint)null);
        } else {
            int startWidth;
            if (alignment == Alignment.ALIGN_CENTER) {
                startWidth = (384 - bitmap1.getWidth()) / 2;
                cv.drawBitmap(bitmap1, (float)startWidth, 0.0F, (Paint)null);
            } else {
                startWidth = 384 - bitmap1.getWidth();
                cv.drawBitmap(bitmap1, (float)startWidth, 0.0F, (Paint)null);
            }
        }

        cv.save();
        cv.restore();
        bitmaps.add(newbmp);
    }

    public Bitmap setPrintStart() {
        Bitmap bitmap = this.coverAllBitmap();
        bitmaps.clear();
        return bitmap;
    }

    public List<Bitmap> setPrintStart1() {
        Log.d("gfgjgf","3 :"+bitmaps);
        List<Bitmap> bitmap = bitmaps;
        //bitmaps.clear();
        return bitmaps;
    }

    public static void saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    public Bitmap coverAllBitmap() {
        int weight = 0;
        int height = 0;

        for(int i = 0; i < bitmaps.size(); ++i) {
            weight = ((Bitmap)bitmaps.get(i)).getWidth();
            height += ((Bitmap)bitmaps.get(i)).getHeight();
        }

        Bitmap bitmap = Bitmap.createBitmap(384, height, Config.ARGB_8888);
        weight = 0;
        height = 0;
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(-1);

        for(int i = 0; i < bitmaps.size(); ++i) {
            canvas.drawBitmap((Bitmap)bitmaps.get(i), (float)weight, (float)height, this.mBitPaint);
            height += ((Bitmap)bitmaps.get(i)).getHeight();
        }

        canvas.save();
        canvas.restore();
        return bitmap;
    }



    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;

        byte[] destArray;
        for(Iterator var3 = srcArrays.iterator(); var3.hasNext(); len += destArray.length) {
            destArray = (byte[])var3.next();
        }

        destArray = new byte[len];
        int destLen = 0;

        byte[] srcArray;
        for(Iterator var5 = srcArrays.iterator(); var5.hasNext(); destLen += srcArray.length) {
            srcArray = (byte[])var5.next();
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
        }

        return destArray;
    }

//    public Bitmap createQRImage(String content, int widthPix, int heightPix, Alignment alignment) {
//        Bitmap bitmap = createQRCode(content, widthPix, heightPix);
//        if (bitmap == null) {
//            return null;
//        } else {
//            Bitmap newbmp = Bitmap.createBitmap(384, bitmap.getHeight(), Config.ARGB_8888);
//            Canvas cv = new Canvas(newbmp);
//            if (alignment == Alignment.ALIGN_NORMAL) {
//                cv.drawBitmap(bitmap, 0.0F, 0.0F, (Paint)null);
//            } else {
//                int startWidth;
//                if (alignment == Alignment.ALIGN_CENTER) {
//                    startWidth = (384 - bitmap.getWidth()) / 2;
//                    cv.drawBitmap(bitmap, (float)startWidth, 0.0F, (Paint)null);
//                } else {
//                    startWidth = 384 - bitmap.getWidth();
//                    cv.drawBitmap(bitmap, (float)startWidth, 0.0F, (Paint)null);
//                }
//            }
//
//            cv.save();
//            cv.restore();
//            return newbmp;
//        }
//    }

//    public static Bitmap createQRCode(String content, int width, int height) {
//        if (content != null && !"".equals(content)) {
//            Map<EncodeHintType, Object> hints = new HashMap();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            hints.put(EncodeHintType.MARGIN, 1);
//            Bitmap bitmap = null;
//
//            try {
//                BitMatrix bitMatrix = (new QRCodeWriter()).encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//                int[] pixels = new int[width * height];
//
//                for(int y = 0; y < height; ++y) {
//                    for(int x = 0; x < width; ++x) {
//                        if (bitMatrix.get(x, y)) {
//                            pixels[y * width + x] = -16777216;
//                        } else {
//                            pixels[y * width + x] = -1;
//                        }
//                    }
//                }
//
//                bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//                bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//                return bitmap;
//            } catch (WriterException var9) {
//                throw new IllegalArgumentException("CreateQRCode failed", var9);
//            }
//        } else {
//            return null;
//        }
//    }
//
//    private static BitMatrix deleteWhite(BitMatrix matrix) {
//        int[] rec = matrix.getEnclosingRectangle();
//        int resWidth = rec[2] + 1;
//        int resHeight = rec[3] + 1;
//        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
//        resMatrix.clear();
//
//        for(int i = 0; i < resWidth; ++i) {
//            for(int j = 0; j < resHeight; ++j) {
//                if (matrix.get(i + rec[0], j + rec[1])) {
//                    resMatrix.set(i, j);
//                }
//            }
//        }
//
//        return resMatrix;
//    }
//
//    public Bitmap creatBarcode(Context context, String contents, int desiredWidth, int desiredHeight, boolean displayCode, Alignment alignment, BarcodeFormat format) {
//        Bitmap ruseltBitmap = null;
//        barcodeFormat = format;
//        Bitmap newbmp;
//        if (displayCode) {
//            newbmp = null;
//
//            try {
//                newbmp = encodeAsBitmap(contents, format, desiredWidth, desiredHeight);
//            } catch (WriterException var13) {
//                var13.printStackTrace();
//            }
//
//            Bitmap codeBitmap = createCodeBitmap(contents, desiredWidth, desiredHeight, context);
//            ruseltBitmap = mixtureBitmap(newbmp, codeBitmap, new PointF(0.0F, (float)desiredHeight));
//        } else {
//            try {
//                ruseltBitmap = encodeAsBitmap(contents, format, desiredWidth, desiredHeight);
//            } catch (WriterException var12) {
//                var12.printStackTrace();
//            }
//        }
//
//        newbmp = Bitmap.createBitmap(384, ruseltBitmap.getHeight(), Config.ARGB_8888);
//        Canvas cv = new Canvas(newbmp);
//        if (alignment == Alignment.ALIGN_NORMAL) {
//            cv.drawBitmap(ruseltBitmap, 0.0F, 0.0F, (Paint)null);
//        } else {
//            int startWidth;
//            if (alignment == Alignment.ALIGN_CENTER) {
//                startWidth = (384 - ruseltBitmap.getWidth()) / 2;
//                cv.drawBitmap(ruseltBitmap, (float)startWidth, 0.0F, (Paint)null);
//            } else {
//                startWidth = 384 - ruseltBitmap.getWidth();
//                cv.drawBitmap(ruseltBitmap, (float)startWidth, 0.0F, (Paint)null);
//            }
//        }
//
//        cv.save();
//        cv.restore();
//        return newbmp;
//    }

    public static Bitmap createCodeBitmap(String contents, int width, int height, Context context) {
        TextView tv = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(width, height);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(48);
        tv.setTextSize(18.0F);
        tv.setGravity(1);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(-16777216);
        tv.setBackgroundColor(-1);
        tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());
        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }

//    public static Bitmap encode2dAsBitmap(String contents, int desiredWidth, int desiredHeight, int barType) {
//        BarcodeFormat format = barcodeFormat;
//        if (barType == 1) {
//            format = BarcodeFormat.CODE_128;
//        } else if (barType == 2) {
//            format = BarcodeFormat.QR_CODE;
//        }
//
//        Bitmap barcodeBitmap = null;
//
//        try {
//            barcodeBitmap = encodeAsBitmap(contents, format, desiredWidth, desiredHeight);
//        } catch (WriterException var7) {
//            var7.printStackTrace();
//        }
//
//        return barcodeBitmap;
//    }

    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first != null && second != null && fromPoint != null) {
            Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight() + second.getHeight(), Config.ARGB_4444);
            Canvas cv = new Canvas(newBitmap);
            cv.drawBitmap(first, 0.0F, 0.0F, (Paint)null);
            cv.drawBitmap(second, fromPoint.x, fromPoint.y, (Paint)null);
            cv.save();
            cv.restore();
            return newBitmap;
        } else {
            return null;
        }
    }

//    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) throws WriterException {
//        Map<EncodeHintType, Object> hints = new HashMap();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        hints.put(EncodeHintType.MARGIN, 25);
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
//        int width = result.getWidth();
//        int height = result.getHeight();
//        int[] pixels = new int[width * height];
//
//        for(int y = 0; y < height; ++y) {
//            int offset = y * width;
//
//            for(int x = 0; x < width; ++x) {
//                pixels[offset + x] = result.get(x, y) ? -16777216 : -1;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }

    public static String guessAppropriateEncoding(CharSequence contents) {
        for(int i = 0; i < contents.length(); ++i) {
            if (contents.charAt(i) > 255) {
                return "UTF-8";
            }
        }

        return null;
    }
}

