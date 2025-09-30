package sensors;

import java.util.Arrays;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.AggregateIterable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class Sensors {

    final static int PORT = 27017;
    final static int TIMEOUT = 3000;
    final static String dbName = "sensors";
    final static String collectionName = "data";
    final static String HOST_NAME = "localhost";

    public static void main(String[] args) {

        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        long begin = System.currentTimeMillis();
        InetAddress ip;
        try {
            ip = InetAddress.getByName(HOST_NAME);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, PORT), TIMEOUT);
        } catch (UnknownHostException uhe) {
            System.out.println("Unknown host name!");
            return;
        } catch (IOException iox) {
            System.out.println("MongoDB server is not wotking!");
            return;
        }

        MongoClient mongoClient = new MongoClient(
                new MongoClientURI(
                        "mongodb://localhost:27017/"
                )
        );
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                new Document("type", "humidity")),
                new Document("$sort",
                        new Document("sensorId", 1L)),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("value", 1L)
                                .append("sensorId", 1L))));

        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the type of sensor:");
            String sensorType = buffer.readLine();
            String units;
            switch (sensorType) {
                case "temperature":
                    units = "\u00B0C";
                    break;
                case "humidity":
                    units = "%";
                    break;
                case "pressure":
                    units = "mb";
                    break;
                default:
                    units = null;
                    throw new IOException("Invalid type of sensor!");
            }

            for (Document document : result) {
                String json = document.toJson();
                JSONObject obj = new JSONObject(json);
                int id = obj.getInt("sensorId");
                int value = obj.getInt("value");
                long end = System.currentTimeMillis();
                long time = end - begin;
                System.out.println("Elapsed Time: " + time + " milli seconds");
                System.out.println("Sensor " + id + ": " + sensorType + " " + value + units);
            }

        } catch (IOException ioe) {
            System.out.println("Invalid type of sensor!");
        }
    }
}
