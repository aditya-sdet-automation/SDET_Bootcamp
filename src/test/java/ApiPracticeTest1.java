/**
 * Sample test to understand API(Application Programming Interface) GET
 */

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*; //critical line,else given() statements won't work

public class ApiPracticeTest1 {
    @Test
    public void verifyGetUsers(){
        given()
                .when()
                .get("https://reqres.in/api/users")
                .then().log().all()
                .statusCode(200);
        // given() left empty for simple get requests
        // when() action you take like get or post
        // then() assertions
    }
}
