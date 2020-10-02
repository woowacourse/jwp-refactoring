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

	@DisplayName("2 개 이상의 빈 테이블을 단체로 지정할 수 있다.")
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
			.body("id", equalTo(2))
			.body("createdDate", notNullValue())
			.body("orderTables", hasSize(2));
	}

	@DisplayName("단체 지정은 중복될 수 없다.")
	@Test
	void create2() {
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
			.body("id", equalTo(2))
			.body("createdDate", notNullValue())
			.body("orderTables", hasSize(2));
	}
}
