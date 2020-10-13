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
			.body("id", notNullValue())
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

	@DisplayName("단체 지정을 해지할 수 있다.")
	@Test
	void ungroup() {
		given().log().all()
			.pathParam("tableGroupId", 1)
			.when()
			.delete("/api/table-groups/{tableGroupId}")
			.then().log().all()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 조리인 경우 단체 지정을 해지할 수 없다.")
	@Test
	void ungroup_WhenOrderIsCooking() {
		given().log().all()
			.pathParam("tableGroupId", 2)
			.when()
			.delete("/api/table-groups/{tableGroupId}")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("단체 지정된 주문 테이블의 주문 상태가 식사인 경우 단체 지정을 해지할 수 없다.")
	@Test
	void ungroup_WhenOrderIsMeal() {
		given().log().all()
			.pathParam("tableGroupId", 3)
			.when()
			.delete("/api/table-groups/{tableGroupId}")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
