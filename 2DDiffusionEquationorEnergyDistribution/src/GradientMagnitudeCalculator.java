public class GradientMagnitudeCalculator {
    public static void main(String[] args) {
        double[][] matrix = {
                { 0.25, 0.5, 0.25 },
                { 0.5, 1.0, 0.5 },
                { 0.25, 0.5, 0.25 },
        };

        double[][] gradientX = calculateGradientX(matrix);
        double[][] gradientY = calculateGradientY(matrix);
        double[][] magnitude = calculateGradientMagnitude(gradientX, gradientY);

        System.out.println("Gradient X:");
        printMatrix(gradientX);

        System.out.println("Gradient Y:");
        printMatrix(gradientY);

        System.out.println("Gradient Magnitude:");
        printMatrix(magnitude);
    }

    // Градиент по строкам
    public static double[][] calculateGradientX(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] gradientX = new double[rows][cols];

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                gradientX[i][j] = (matrix[i + 1][j] - matrix[i - 1][j]) / 2.0;
            }
        }

        // Краевые элементы
        for (int j = 0; j < cols; j++) {
            gradientX[0][j] = matrix[1][j] - matrix[0][j];
            gradientX[rows - 1][j] = matrix[rows - 1][j] - matrix[rows - 2][j];
        }

        return gradientX;
    }

    // Градиент по столбцам
    public static double[][] calculateGradientY(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] gradientY = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 1; j < cols - 1; j++) {
                gradientY[i][j] = (matrix[i][j + 1] - matrix[i][j - 1]) / 2.0;
            }
        }

        // Краевые элементы
        for (int i = 0; i < rows; i++) {
            gradientY[i][0] = matrix[i][1] - matrix[i][0];
            gradientY[i][cols - 1] = matrix[i][cols - 1] - matrix[i][cols - 2];
        }

        return gradientY;
    }

    // Вычисление магнитуды градиента
    public static double[][] calculateGradientMagnitude(double[][] gradientX, double[][] gradientY) {
        int rows = gradientX.length;
        int cols = gradientX[0].length;
        double[][] magnitude = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                magnitude[i][j] = Math.sqrt(gradientX[i][j] * gradientX[i][j] + gradientY[i][j] * gradientY[i][j]);
            }
        }

        return magnitude;
    }

    // Печать матрицы
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%6.2f ", value);
            }
            System.out.println();
        }
    }
}
