package example;

import org.neo4j.graphdb.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;
//import org.neo4j.driver.*;

public class countNode {
    @Context
    public org.neo4j.graphdb.GraphDatabaseService db;

    @Procedure(value = "example.countNode")
    @Description("Count the total node")
    public Stream<Count> countNode(@Name("label") String labelName){
        Label label = Label.label(labelName);
//        int count=1;
//        try ( Transaction tx = db.beginTx() ) {
//	        ResourceIterator<Node> users = tx.findNodes(label);
//	        while(users.hasNext()){
//	            count++;
//	            users= (ResourceIterator<Node>) users.next();
//	        }
//	        return count;
//        }

//        try (Transaction tx = db.beginTx() ) {
//            ResourceIterator<Node> users = tx.findNodes(label);
//            while(users.hasNext()){
//
//            }
//        }
//            return;
        Count count = new Count(labelName);
        try (Transaction tx = db.beginTx()) {
            ResourceIterator<Node> users = tx.findNodes(label);
            while (users.hasNext()){
                count.add(users.next().getLabels() .toString());
            }
        }
        return Stream.of(count);
        }
    public static class Count {
        public final String label;
        public long count;
        //constructor
        Count(String label) { this.label = label; }

        void add(String label) {
            count ++;
        }
    }
}