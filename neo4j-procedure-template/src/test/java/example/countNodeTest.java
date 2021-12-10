package example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.*;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import static org.junit.Assert.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class countNodeTest {

    private static final Config driverConfig = Config.builder().withoutEncryption().build();
    private Neo4j embeddedDatabaseServer;
    private static Driver driver;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withProcedure(countNode.class)
                .build();
        this.driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI(), driverConfig);

    }
    @AfterAll
    void closeDriver(){
        this.driver.close();
        this.embeddedDatabaseServer.close();
    }

    @Test
    void getCountNode() {
        // This is in a try-block, to make sure we close the driver after the test
        try(Session session = driver.session()) {

            int count = 1;
            session.run("CREATE(:Place{id:1})");

//            Result record = session.run("MATCH (u:User) " +
//                    "CALL bz.countNode('User') YIELD count " +
//                    "RETURN count").single();
            Result record = session.run("MATCH (u:Place) " +
                   "CALL example.countNode('Place') YIELD count " +
                  "RETURN count");



            assertThat( (record.peek().get(0)).asInt()).isEqualTo(1);
        }
    }
}