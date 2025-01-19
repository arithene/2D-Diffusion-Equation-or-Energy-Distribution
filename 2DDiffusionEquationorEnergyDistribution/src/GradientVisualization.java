import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GradientVisualization {

    public static void main(String[] args) {
        double[][] data = new double[9][9];
        Random rand = new Random();

        // Fill the array with random values
        // for (int i = 0; i < 9; i++) {
        // for (int j = 0; j < 9; j++) {
        // data[i][j] = rand.nextDouble() * 100; // values between 0 and 100
        // }
        // }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Вычисляем расстояние от текущей ячейки до центра (4,4)
                double distance = Math.sqrt(Math.pow(i - 4, 2) + Math.pow(j - 4, 2));
                // Максимальное расстояние от центра (по диагонали)
                double maxDistance = Math.sqrt(2 * Math.pow(4, 2));
                // Яркость уменьшается по мере увеличения расстояния от центра
                data[i][j] = (1 - (distance / maxDistance)) * 100; // значения между 0 и 100
            }
        }

        // Compute the gradient along the Z-axis (difference between neighboring values)
        double[][] gradientZ = new double[9][9];
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                double gradX = (data[i + 1][j] - data[i - 1][j]) / 2.0;
                double gradY = (data[i][j + 1] - data[i][j - 1]) / 2.0;
                gradientZ[i][j] = Math.sqrt(gradX * gradX + gradY * gradY);
            }
        }

        // Create a custom JPanel to visualize the gradient
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int squareSize = 50; // size of each square
                for (int i = 1; i < 8; i++) {
                    for (int j = 1; j < 8; j++) {
                        // Map gradient value to a color
                        float value = (float) gradientZ[i][j];
                        Color color = getColorForValue(value);
                        g.setColor(color);
                        g.fillRect((j - 1) * squareSize, (i - 1) * squareSize, squareSize, squareSize);
                    }
                }
            }

            // Helper method to map a gradient value to a color
            private Color getColorForValue(float value) {
                int r = Math.min(255, (int) (value * 2)); // Red increases with value
                int g = Math.min(255, (int) (255 - value * 2)); // Green decreases with value
                int b = Math.min(255, (int) (255 - value * 2)); // Blue decreases with value
                return new Color(r, 0, 0);
            }
        };

        // Set up the JFrame
        JFrame frame = new JFrame("Gradient Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
