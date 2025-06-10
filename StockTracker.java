import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class StockTracker {
    private static final String API_KEY = "3385a510801d462b8225af3b7c3d062a";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter stock symbol (e.g., AAPL, TSLA): ");
        String symbol = scanner.nextLine().trim();

        String urlStr = "https://api.twelvedata.com/quote?symbol=" + symbol + "&apikey=" + API_KEY;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                System.out.println("❌ API request failed. Check symbol or API key.");
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                json.append(line);
            }
            in.close();

            String response = json.toString();
            System.out.println("\n📊 Stock Data for: " + symbol.toUpperCase());
            System.out.println("Price      : $" + extract(response, "\"price\":\"", "\""));
            System.out.println("Open       : $" + extract(response, "\"open\":\"", "\""));
            System.out.println("High       : $" + extract(response, "\"high\":\"", "\""));
            System.out.println("Low        : $" + extract(response, "\"low\":\"", "\""));
            System.out.println("Volume     : " + extract(response, "\"volume\":\"", "\""));
            System.out.println("Exchange   : " + extract(response, "\"exchange\":\"", "\""));

        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    private static String extract(String json, String start, String end) {
        try {
            int startIndex = json.indexOf(start) + start.length();
            int endIndex = json.indexOf(end, startIndex);
            return json.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "N/A";
        }
    }
}

