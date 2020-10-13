package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static kitchenpos.integrationtest.step.OrderIntegrationTestStep.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

@DisplayName("Order 통합 테스트")
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
			.statusCode(HttpStatus.CREATED.value())
			.body("id", notNullValue())
			.body("orderTableId", equalTo(2))
			.body("orderStatus", equalTo("COOKING"))
			.body("orderedTime", notNullValue())
			.body("orderLineItems", hasSize(1));
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

	@DisplayName("주문의 목록을 조회할 수 있다.")
	@Test
	void list() {
		given().log().all()
			.accept(ContentType.JSON)
			.when()
			.get("/api/orders")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.body(".", hasSize(5));
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderStatus", "MEAL");

		given().log().all()
			.pathParam("orderId", 1)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/orders/{orderId}/order-status")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.body("orderStatus", equalTo("MEAL"));
	}

	@DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
	@Test
	void changeOrderStatus_WhenOrderStatusIsCompletion() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("orderStatus", "MEAL");

		given().log().all()
			.pathParam("orderId", 3)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/orders/{orderId}/order-status")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
