/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication23;

import java.util.Arrays;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.UpdateOptions;
import static com.mongodb.client.model.Updates.set;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import java.util.concurrent.TimeUnit;
import org.bson.Document;
import org.json.JSONException;

public class JavaApplication23 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String DATABASE_NAME = "students";
        final String COLLECTION_NAME = "data1";
        final String STUDENT_ID = "21705001";
        final String SUBJECT_NAME = "НБД";
        final int GRADE = 6;

        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
            Bson query = new Document();
            Bson update = set("grade.$[elem].value", GRADE);
            UpdateOptions options = new UpdateOptions()
                    .arrayFilters(
                            Arrays.asList(Filters.in("elem.subject", Arrays.asList(SUBJECT_NAME)))
                    );
            collection.updateMany(query, update, options);
            System.out.println("Document update successfully...");
        } catch (JSONException e) {
            System.out.println(e);
        }
    }
}
