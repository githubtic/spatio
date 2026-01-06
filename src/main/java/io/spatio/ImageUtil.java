package io.spatio;

import javax.imageio.ImageIO;
import java.awt.*;
        import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static void saveAsPng100x100(BufferedImage source, String path) {
        try {
            int targetWidth = 100;
            int targetHeight = 100;

            BufferedImage resized = new BufferedImage(
                    targetWidth,
                    targetHeight,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g2d = resized.createGraphics();

            // Qualité de redimensionnement (bonne pratique)
            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            g2d.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2d.drawImage(source, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            ImageIO.write(resized, "png", new File(path));

        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde PNG 100x100", e);
        }
    }
}