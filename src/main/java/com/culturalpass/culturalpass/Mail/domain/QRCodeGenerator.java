package com.culturalpass.culturalpass.Mail.domain;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class QRCodeGenerator {

    public static byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int grayValue = (bitMatrix.get(x, y) ? 0 : 255);
                int rgb = (grayValue << 16) | (grayValue << 8) | grayValue;
                bufferedImage.setRGB(x, y, rgb);
            }
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}