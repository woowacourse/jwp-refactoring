package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static kitchenpos.integrationtest.step.OrderIntegrationTestStep.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

public class OrderIntegrationTest extends IntegrationTest {

	@DisplayName("1 개 이상의 등록된 메뉴로 주문을 등록할 수 있다.")
	@Test
	void create() {
		Map<String, Object> requestBody = createValidOrder();

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/orders")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value());
	}

	@DisplayName("빈 테이블에는 주문을 등록할 수 없다.")
	@Test
	void create_WhenAddToEmptyTable() {
		Map<String, Object> requestBody = createOrderToEmptyTable();

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/orders")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
