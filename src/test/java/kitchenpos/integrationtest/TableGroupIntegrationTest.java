package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

@DisplayName("TableGroup 통합 테스트")
public class TableGroupIntegrationTest extends IntegrationTest {

	@Test
	void create() {
		Map<String, Object> orderTables1 = new HashMap<>();
		orderTables1.put("id", 1);

		Map<String, Object> orderTables2 = new HashMap<>();
		orderTables2.put("id", 3);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderTables", Arrays.asList(
			orderTables1,
			orderTables2
		));

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/table-groups")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.assertThat().body("id", equalTo(2))
			.assertThat().body("createdDate", notNullValue())
			.assertThat().body("orderTables", hasSize(2));

	}
}
