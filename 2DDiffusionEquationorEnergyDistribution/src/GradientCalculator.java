public class GradientCalculator {

    public static double[] calculateGradient(double[] array) {
        int length = array.length;
        double[] gradient = new double[length - 1];

        for (int i = 0; i < length - 1; i++) {
            gradient[i] = array[i + 1] - array[i];
        }

        return gradient;
    }

    public static void main(String[] args) {
        double[] data = { 1.0, 2.0, 4.0, 7.0, 11.0 };
        double[] gradient = calculateGradient(data);

        // Выводим результат
        for (double g : gradient) {
            System.out.println(g);
        }
    }
}
