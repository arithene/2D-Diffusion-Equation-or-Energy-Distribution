import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DiffusionPanel extends JPanel {
    private final int width = 80; // Ширина сетки
    private final int height = width / 2; // Высота сетки
    private double[][] grid; // Текущая концентрация
    private double[][] newGrid; // Новая концентрация
    private final double diffusionRate = 0.05f; // Скорость диффузии
    private boolean INVERTED = false;
    private static double AMPLITUDE = 1;
    private int x;
    private int y;

    public DiffusionPanel() {
        grid = new double[width + 2][height + 2]; // Сетка с фиктивными ячейками
        newGrid = new double[width + 2][height + 2];
        setPreferredSize(new Dimension(1920, 1080)); // Размер панели
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    INVERTED = false; // Переключение флага при нажатии
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    INVERTED = true; // Переключение флага при нажатии
                }
                x = e.getX() * width / getWidth() + 1;
                y = e.getY() * height / getHeight() + 1;
            }
        });
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX() * width / getWidth() + 1;
                y = e.getY() * height / getHeight() + 1;
                if (x > 0 && x <= width && y > 0 && y <= height) {
                    grid[x][y] = INVERTED ? -AMPLITUDE : AMPLITUDE; // Максимальная концентрация
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        diffusion_dt(2.5d, 1d);

        // Рисование сетки
        int cellWidth = getWidth() / width;
        int cellHeight = getHeight() / height;
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                // g2d.setColor(interpolateColor(Color.BLUE, Color.RED, ((grid[x][y] +
                // AMPLITUDE) / (2.0f * AMPLITUDE))));
                g2d.setColor(interpolateColor(Color.BLUE, Color.BLACK, Color.RED, ((grid[x][y] +
                        AMPLITUDE) / (2.0f * AMPLITUDE))));
                // g2d.setColor(interpolateInfernoColor((grid[x][y] + AMPLITUDE) / (2.0f *
                // AMPLITUDE)));
                int left = (x - 1) * cellWidth;
                int top = (y - 1) * cellHeight;
                int right = x * cellWidth;
                int bottom = y * cellHeight;

                // Рисуем квадрат
                g2d.fillRect(left, top, right - left, bottom - top);

                // // Рисуем текст с текущим значением ячейки
                String value = String.format("%.1f", grid[x][y]);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                FontMetrics fm = g2d.getFontMetrics();
                int textX = left + (right - left - fm.stringWidth(value)) / 2;
                int textY = top + (bottom - top + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(value, textX, textY);
            }
        }

        // Перерисовка
        Toolkit.getDefaultToolkit().sync();
    }

    private void diffusion_dt(
            double deltaT, // Шаг по времени
            double deltaX// Шаг по пространству (равномерная сетка)
    ) {
        // Обновление фиктивных ячеек (отражение)
        for (int x = 1; x <= width; x++) {
            grid[x][0] = grid[x][1]; // Верхняя граница
            grid[x][height + 1] = grid[x][height]; // Нижняя граница
        }
        for (int y = 1; y <= height; y++) {
            grid[0][y] = grid[1][y]; // Левая граница
            grid[width + 1][y] = grid[width][y]; // Правая граница
        }
        // Углы
        grid[0][0] = grid[1][1];
        grid[0][height + 1] = grid[1][height];
        grid[width + 1][0] = grid[width][1];
        grid[width + 1][height + 1] = grid[width][height];

        // Учет временного шага
        double factor = deltaT / (deltaX * deltaX);

        // Обновление внутренней области сетки
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                // Соседние значения
                double laplacian = grid[x + 1][y] + grid[x - 1][y] +
                        grid[x][y + 1] + grid[x][y - 1] -
                        4 * grid[x][y];

                // Обновление с учетом временного шага и коэффициента диффузии
                newGrid[x][y] = grid[x][y] + factor * diffusionRate * laplacian;
            }
        }

        // Копирование новых значений в основную сетку
        for (int x = 1; x <= width; x++) {
            System.arraycopy(newGrid[x], 1, grid[x], 1, height);
        }
    }

    private void diffusion() {
        // Обновление фиктивных ячеек (отражение)
        for (int x = 1; x <= width; x++) {
            grid[x][0] = grid[x][1]; // Верхняя граница
            grid[x][height + 1] = grid[x][height]; // Нижняя граница
        }
        for (int y = 1; y <= height; y++) {
            grid[0][y] = grid[1][y]; // Левая граница
            grid[width + 1][y] = grid[width][y]; // Правая граница
        }
        // Углы
        grid[0][0] = grid[1][1];
        grid[0][height + 1] = grid[1][height];
        grid[width + 1][0] = grid[width][1];
        grid[width + 1][height + 1] = grid[width][height];

        // Обновление внутренней области сетки
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                float sum = 0;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0)
                            continue; // Пропуск центральной ячейки
                        sum += grid[x + dx][y + dy];
                    }
                }
                newGrid[x][y] = grid[x][y] + diffusionRate * (sum - 8 * grid[x][y]);
            }
        }

        // Копирование новых значений в основную сетку
        for (int x = 1; x <= width; x++) {
            System.arraycopy(newGrid[x], 1, grid[x], 1, height);
        }

    }

    private Color interpolateInfernoColor(double factor) {
        factor = Math.min(1f, Math.max(0f, factor)); // Ограничение [0, 1]

        // Начальные и конечные значения для палитры Inferno
        Color[] infernoPalette = new Color[256];

        // Определяем фиксированные начальные и конечные значения цветов для схемы
        // Inferno
        int[][] colors = {
                { 0, 0, 4 }, { 43, 0, 35 }, { 116, 14, 79 }, { 213, 51, 107 }, { 254, 97, 36 },
                { 255, 255, 0 }, { 255, 255, 255 }
        };

        // Интерполяция цветов от первого к последнему
        for (int i = 0; i < 256; i++) {
            // Находим, какие два цвета для интерполяции
            double scaledFactor = i / 255.0;
            int index1 = (int) (scaledFactor * (colors.length - 1));
            int index2 = Math.min(index1 + 1, colors.length - 1);

            double localFactor = (scaledFactor - (index1 / (double) (colors.length - 1))) * (colors.length - 1);

            int r = (int) (colors[index1][0] + localFactor * (colors[index2][0] - colors[index1][0]));
            int g = (int) (colors[index1][1] + localFactor * (colors[index2][1] - colors[index1][1]));
            int b = (int) (colors[index1][2] + localFactor * (colors[index2][2] - colors[index1][2]));

            infernoPalette[i] = new Color(r, g, b);
        }

        // Интерполируем на основе полученной палитры
        int startIndex = Math.min((int) (factor * 255), 255);
        return infernoPalette[startIndex];
    }

    private Color interpolateInfernoColor12(double factor) {
        factor = Math.min(1f, Math.max(0f, factor)); // Ограничение [0, 1]

        // Расширенная палитра для схемы Inferno с большим количеством цветов
        Color[] infernoPalette = {
                new Color(0, 0, 4), // Темный цвет (почти черный)
                new Color(16, 0, 29), // Очень тёмно-фиолетовый
                new Color(43, 0, 35), // Тёмно-бордовый
                new Color(116, 14, 79), // Бордово-красный
                new Color(186, 24, 67), // Темно-красный
                new Color(213, 51, 107), // Ярко-красный
                new Color(254, 97, 36), // Оранжевый
                new Color(254, 159, 34), // Ярко-оранжевый
                new Color(255, 215, 0), // Желтый
                new Color(255, 254, 0), // Золотисто-желтый
                new Color(255, 255, 0), // Желтый
                new Color(255, 255, 255) // Белый
        };

        // Вычисляем индексы начального и конечного цветов
        int startIndex = Math.min((int) (factor * (infernoPalette.length - 1)), infernoPalette.length - 1);
        int endIndex = Math.min((int) ((factor + 0.05) * (infernoPalette.length - 1)), infernoPalette.length - 1); // Плавный
                                                                                                                   // переход

        Color startColor = infernoPalette[startIndex];
        Color endColor = infernoPalette[endIndex];

        // Интерполяция каждого компонента (красный, зелёный, синий)
        int r = (int) (startColor.getRed() + factor * (endColor.getRed() - startColor.getRed()));
        int g = (int) (startColor.getGreen() + factor * (endColor.getGreen() - startColor.getGreen()));
        int b = (int) (startColor.getBlue() + factor * (endColor.getBlue() - startColor.getBlue()));

        return new Color(r, g, b);
    }

    private Color interpolateInfernoColor7(double factor) {
        factor = Math.min(1f, Math.max(0f, factor)); // Ограничение [0, 1]

        // Цвета для схемы Inferno
        Color[] infernoPalette = {
                new Color(0, 0, 4), // Темный цвет (почти черный)
                new Color(43, 0, 35), // Тёмно-бордовый
                new Color(116, 14, 79), // Бордово-красный
                new Color(213, 51, 107), // Ярко-красный
                new Color(254, 97, 36), // Оранжевый
                new Color(255, 255, 0), // Желтый
                new Color(255, 255, 255) // Белый
        };

        int startIndex = Math.min((int) (factor * (infernoPalette.length - 1)), infernoPalette.length - 1);
        int endIndex = Math.min((int) ((factor + 0.1) * (infernoPalette.length - 1)), infernoPalette.length - 1); // Слегка
                                                                                                                  // сдвигаем
                                                                                                                  // конец
                                                                                                                  // для
                                                                                                                  // плавности
                                                                                                                  // перехода

        Color startColor = infernoPalette[startIndex];
        Color endColor = infernoPalette[endIndex];

        int r = (int) (startColor.getRed() + factor * (endColor.getRed() - startColor.getRed()));
        int g = (int) (startColor.getGreen() + factor * (endColor.getGreen() - startColor.getGreen()));
        int b = (int) (startColor.getBlue() + factor * (endColor.getBlue() - startColor.getBlue()));

        return new Color(r, g, b);
    }

    private Color interpolateColor(Color color1, Color color2, Color color3, double factor) {
        factor = Math.min(1.0, Math.max(0.0, factor)); // Ограничение [0, 1]

        if (factor <= 0.5) {
            // Переход между color1 и color2
            double localFactor = factor * 2; // Нормализация [0, 0.5] -> [0, 1]
            return interpolateColor(color1, color2, localFactor);
        } else {
            // Переход между color2 и color3
            double localFactor = (factor - 0.5) * 2; // Нормализация [0.5, 1] -> [0, 1]
            return interpolateColor(color2, color3, localFactor);
        }
    }

    private Color interpolateColor(Color colorStart, Color colorEnd, double factor) {
        factor = Math.min(1f, Math.max(0f, factor)); // Ограничение [0, 1]
        int startR = colorStart.getRed();
        int startG = colorStart.getGreen();
        int startB = colorStart.getBlue();
        int endR = colorEnd.getRed();
        int endG = colorEnd.getGreen();
        int endB = colorEnd.getBlue();

        int r = (int) (startR + factor * (endR - startR));
        int g = (int) (startG + factor * (endG - startG));
        int b = (int) (startB + factor * (endB - startB));

        return new Color(r, g, b);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Diffusion Simulation");
        DiffusionPanel panel = new DiffusionPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setSize(1920, 1080);
        frame.setVisible(true);
        new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.repaint();
            }
        }).start();
    }
}
