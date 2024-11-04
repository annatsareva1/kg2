import java.awt.image.BufferedImage;

public class ImageProcessor {

    public static BufferedImage enhanceContrast(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        int min = 255, max = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int intensity = inputImage.getRGB(x, y) & 0xFF;
                if (intensity < min) min = intensity;
                if (intensity > max) max = intensity;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int intensity = rgb & 0xFF;
                int newIntensity = (int) ((intensity - min) * 255.0 / (max - min));
                int newRgb = (alpha << 24) | (newIntensity << 16) | (newIntensity << 8) | newIntensity;
                outputImage.setRGB(x, y, newRgb);
            }
        }

        return outputImage;
    }

    public static BufferedImage equalizeHistogram(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        int[] histogram = new int[256];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int intensity = inputImage.getRGB(x, y) & 0xFF;
                histogram[intensity]++;
            }
        }

        int[] cumulativeDistribution = new int[256];
        cumulativeDistribution[0] = histogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeDistribution[i] = cumulativeDistribution[i - 1] + histogram[i];
        }

        int totalPixels = width * height;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int intensity = rgb & 0xFF;
                int newIntensity = cumulativeDistribution[intensity] * 255 / totalPixels;
                int newRgb = (alpha << 24) | (newIntensity << 16) | (newIntensity << 8) | newIntensity;
                outputImage.setRGB(x, y, newRgb);
            }
        }

        return outputImage;
    }

    public static BufferedImage sharpenImage(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        float[] sharpenKernel = {
                0, -1, 0,
                -1, 5, -1,
                0, -1, 0
        };

        int kernelSize = 3;
        int radius = kernelSize / 2;

        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                int newRed = 0, newGreen = 0, newBlue = 0;
                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int rgb = inputImage.getRGB(x + kx, y + ky);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;
                        float kernelValue = sharpenKernel[(ky + radius) * kernelSize + (kx + radius)];
                        newRed += kernelValue * red;
                        newGreen += kernelValue * green;
                        newBlue += kernelValue * blue;
                    }
                }
                newRed = Math.min(Math.max(newRed, 0), 255);
                newGreen = Math.min(Math.max(newGreen, 0), 255);
                newBlue = Math.min(Math.max(newBlue, 0), 255);
                int newArgb = (0xFF << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                outputImage.setRGB(x, y, newArgb);
            }
        }

        return outputImage;
    }

    public static BufferedImage applyThreshold(BufferedImage inputImage, int threshold) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x, y);
                int gray = rgb & 0xFF;
                int newRgb = (gray > threshold) ? 0xFFFFFFFF : 0xFF000000;
                outputImage.setRGB(x, y, newRgb);
            }
        }

        return outputImage;
    }
}
