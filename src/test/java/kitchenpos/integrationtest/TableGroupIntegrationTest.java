package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static kitchenpos.integrationtest.step.TableGroupIntegrationTestStep.*;
import static org.hamcrest.Matchers.*;

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
		Map<String, Object> requestBody = createValidTableGroup();

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

	@DisplayName("단체 지정 시 테이블은 중복될 수 없다.")
	@Test
	void create_WhenTablesAreDuplicated() {
		Map<String, Object> requestBody = createTableGroupThatHasDuplicatedTables();

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/table-groups")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
