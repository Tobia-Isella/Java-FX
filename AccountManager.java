import java.nio.file.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class AccountManager {
    private static final String UUID_FILE = "player_uuid.txt"; // Local file name
    private static final String SERVER_URL = "http://localhost:8080/account/register"; // Your Spring Boot server

    // Returns the current player's UUID, creates one if none exists
    public static String getOrCreateUUID() {
        try {
            Path path = Paths.get(UUID_FILE);

            if (Files.exists(path)) {
                // UUID already exists, load it
                return new String(Files.readAllBytes(path));
            } else {
                // No UUID, generate a new one
                String uuid = UUID.randomUUID().toString();
                Files.write(path, uuid.getBytes());
                return uuid;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Sends the UUID and coin count to the backend server
    public static void registerUUIDWithServer(int coins) {
        try {
            String uuid = getOrCreateUUID();
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
    
            // Create JSON string
            String jsonPayload = String.format("{\"uuid\":\"%s\", \"coins\":%d}", uuid, coins);
    
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes());
            }
    
            int responseCode = conn.getResponseCode();
            System.out.println("Server responded with: " + responseCode);
    
            if (responseCode == 200) {
                System.out.println("UUID and coin data successfully sent to server.");
            } else {
                System.out.println("Failed to send data. Server responded with code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getCoinsFromServer(String uuid) {
        try {
            URL url = new URL("http://localhost:8080/account/" + uuid);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
    
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String response = reader.readLine();
                    return Integer.parseInt(response); // assumes the server returns just the coin amount
                }
            } else if (responseCode == 404) {
                System.out.println("UUID not found on server. Will create new.");
                return 0;
            } else {
                System.out.println("Error retrieving coins: " + responseCode);
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
}
