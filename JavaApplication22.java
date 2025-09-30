/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication22;

import java.util.Arrays;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import org.bson.conversions.Bson;
import java.util.concurrent.TimeUnit;
import org.bson.Document;
import org.json.JSONException;

public class JavaApplication22 {

    final String DATABASE_NAME = "students";
    final String COLLECTION_NAME = "data1";
    final String STUDENT_ID = "21705001";
    final String SUBJECT_NAME = "НБД";
    final int GRADE = 5;

    public static void main(String[] args) {
        final String DATABASE_NAME = "students";
        final String COLLECTION_NAME = "data1";
        final String STUDENT_ID = "21705001";
        final String SUBJECT_NAME = "НБД";
        final int GRADE = 5;
        MongoClient mongoClient = new MongoClient(
                new MongoClientURI(
                        "mongodb://localhost:27017/"
                )
        );
        try {
            MongoDatabase database = mongoClient.getDatabase("students");
            MongoCollection<Document> collection = database.getCollection("data1");
            Bson query = and(eq("id", STUDENT_ID), eq("grade.subject", SUBJECT_NAME));
            Bson update = set("grade.$.value", GRADE);
            collection.updateOne(query, update);
            System.out.println("Document update successfully...");
        } catch (JSONException e) {
            System.out.println(e);
        }

    }
}
