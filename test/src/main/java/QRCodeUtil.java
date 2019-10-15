import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class QRCodeUtil {

    public static final String JPG = "JPG";  // 二维码输出格式
    private static final String CHARSET = "UTF-8";  // 二维码编码字符集
    private static final int QRCODE_SIZE = 300;  // 二维码尺寸
    private static final int LOGO_HEIGHT = 60;  // LOGO高度
    private static final int LOGO_WIDTH = 60;  // LOGO宽度

    /**
     * 根据给定的内容生成二维码
     *
     * @param content        二维码内容
     * @param logoImagePath  logo图标的路径
     * @param needCompressed 是否需要压缩logo
     * @return 生成的二维码
     */
    private static BufferedImage createImage(String content, String logoImagePath, boolean needCompressed)
            throws IOException, WriterException {
        Map<EncodeHintType, Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  // 纠错等级
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);  // 二维码两边空白区域大小
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        if (logoImagePath == null || "".equals(logoImagePath)) {
            return image;
        }
        // 如果有logo，则插入logo图片
        insertImage(image, logoImagePath, needCompressed);
        return image;
    }

    /**
     * 插入logo图片
     *
     * @param sourceImage    原图片
     * @param logoImagePath  logo图片所在的路径
     * @param needCompressed 是否需要压缩
     */
    private static void insertImage(BufferedImage sourceImage, String logoImagePath, boolean needCompressed)
            throws IOException {
        File file = new File(logoImagePath);
        if (!file.exists()) {
            log.warn("logo文件不存在!");
            return;
        }

        Image src = ImageIO.read(file);
        int width = src.getWidth(null);
        int height = src.getHeight(null);

        // 压缩二维码图片
        if (needCompressed) {
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }

            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();  // 释放占有的资源
            src = image;
        }

        // 直观的理解：Graphics2D 就相当于画笔，而BufferedImage 就是画笔绘制的结果
        // 插入logo
        Graphics2D graph = sourceImage.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 生成带logo的二维码
     *
     * @param content        二维码内容
     * @param logoImagePath  logo图片路径
     * @param needCompressed 是否需要压缩
     * @return BufferedImage 二维码图片
     */
    public static BufferedImage encode(String content, String logoImagePath, boolean needCompressed)
            throws IOException, WriterException {
        return createImage(content, logoImagePath, needCompressed);
    }

    /**
     * 生成不带logo的二维码
     *
     * @param content 二维码内容
     * @return BufferedImage 二维码图片
     */
    public static BufferedImage encode(String content) throws Exception {
        return encode(content, null, false);
    }

    /**
     * 解析二维码内容
     *
     * @param file 二维码
     * @return 二维码包含的信息
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hints = Maps.newHashMap();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     * 解析二维码内容
     *
     * @param path 二维码所在位置
     * @return 二维码包含的信息
     */
    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }

}