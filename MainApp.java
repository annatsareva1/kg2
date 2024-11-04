import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class MainApp extends JFrame {
    private JLabel imageLabel;
    private BufferedImage originalImage;
    private BufferedImage processedImage;

    public MainApp() {
        setTitle("Image Processing App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton loadButton = new JButton("Load Image");
        loadButton.addActionListener(e -> loadImage());

        JButton enhanceContrastButton = new JButton("Enhance Contrast");
        enhanceContrastButton.addActionListener(e -> enhanceContrast());

        JButton histogramEqualizeButton = new JButton("Histogram Equalization");
        histogramEqualizeButton.addActionListener(e -> histogramEqualization());

        JButton sharpenButton = new JButton("Sharpen Image");
        sharpenButton.addActionListener(e -> sharpenImage());

        JButton thresholdButton = new JButton("Thresholding");
        thresholdButton.addActionListener(e -> applyThreshold());

        imageLabel = new JLabel();

        JPanel panel = new JPanel();
        panel.add(loadButton);
        panel.add(enhanceContrastButton);
        panel.add(histogramEqualizeButton);
        panel.add(sharpenButton);
        panel.add(thresholdButton);

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(imageLabel), BorderLayout.CENTER);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser("./");
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(file);
                processedImage = originalImage;
                displayImage(originalImage);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage());
            }
        }
    }

    private void enhanceContrast() {
        if (originalImage != null) {
            processedImage = ImageProcessor.enhanceContrast(originalImage);
            displayImage(processedImage);
        }
    }

    private void histogramEqualization() {
        if (originalImage != null) {
            processedImage = ImageProcessor.equalizeHistogram(originalImage);
            displayImage(processedImage);
        }
    }

    private void sharpenImage() {
        if (originalImage != null) {
            processedImage = ImageProcessor.sharpenImage(originalImage);
            displayImage(processedImage);
        }
    }

    private void applyThreshold() {
        if (originalImage != null) {
            processedImage = ImageProcessor.applyThreshold(originalImage, 128);
            displayImage(processedImage);
        }
    }

    private void displayImage(BufferedImage image) {
        if (image != null) {
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            imageLabel.revalidate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}
