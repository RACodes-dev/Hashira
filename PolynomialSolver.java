import java.io.nio.file.Files;
import java.io.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

public class PolynomialSolver {

    static class Point {
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {  
            return "(" + x + ", " + y + ")";
        }
    }

    public static void main(String[] args) {
        try {
    
            String fileName = "input.json";
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONObject json = new JSONObject(content);

            int k = json.getJSONObject("keys").getInt("k");

            List<Point> points = new ArrayList<>();
            Iterator<String> keys = json.keys();

            System.out.println("Decoding points from JSON...");
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.matches("\\d+")) {
                    JSONObject rootObject = json.getJSONObject(key);
                    int base = Integer.parseInt(rootObject.getString("base"));
                    String value = rootObject.getString("value");

                    double x = Double.parseDouble(key);
                    double y = Long.parseLong(value, base);

                    points.add(new Point(x, y));
                    System.out.println("Decoded root: " + points.get(points.size() - 1));
                }
            }

            if (points.size() < k) {
                System.out.println("Error: Not enough points in the JSON to solve the polynomial. Need at least " + k);
                return;
            }

           List<Point> pointsForCalculation = points.subList(0, k);
            
            double secretC = lagrangeInterpolate(pointsForCalculation, 0.0);

            System.out.println("\n-----------------------------------------");
            System.out.println("The calculated secret (c) is: " + secretC);
            System.out.println("-----------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double lagrangeInterpolate(List<Point> points, double x) {
        double result = 0.0;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double term = points.get(i).y;
            
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term = term * (x - points.get(j).x) / (points.get(i).x - points.get(j).x);
                }
            }
            result += term;
        }

        return result;
    }
}