/**
 * Sample test to understand API(Application Programming Interface) POST
 */

import org.openqa.selenium.json.Json;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*; //critical line,else given() statements won't work

public class ApiPracticeTest2 {
    @Test
    public void postUsers(){
        String profile = "{\n" + "    \"name\": \"Reena\",\n" + "    \"job\": \"QA Learner\"\n" + "}";
        String extractedID = given()
                .header("Content-Type", "application/json") //tells server to expect JSON Format
                .body(profile)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .extract().path("id");
        // given() left empty for simple get requests
        // when() action you take like get or post
        // then() assertions
        System.out.println("My created ID is : " + extractedID);
    }
}
