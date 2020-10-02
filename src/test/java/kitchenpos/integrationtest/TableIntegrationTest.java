package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

public class TableIntegrationTest extends IntegrationTest {

	@DisplayName("주문 테이블을 등록할 수 있다.")
	@Test
	void create() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("tableGroupId", null);
		requestBody.put("numberOfGuests", 0);
		requestBody.put("empty", true);

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/tables")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.assertThat().body("id", equalTo(9))
			.assertThat().body("tableGroupId", equalTo(null))
			.assertThat().body("numberOfGuests", equalTo(0))
			.assertThat().body("empty", equalTo(true));
	}

	@DisplayName("주문 테이블의 목록을 조회할 수 있다.")
	@Test
	void list() {
		given().log().all()
			.accept(ContentType.JSON)
			.when()
			.get("/api/tables")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.assertThat().body(".", hasSize(8));
	}

	@DisplayName("빈 테이블을 해지할 수 있다.")
	@Test
	void setEmptyTrue() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("empty", false);

		given().log().all()
			.pathParam("orderTableId", 1)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/tables/{orderTableId}/empty")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.assertThat().body("empty", equalTo(false));
	}

	@DisplayName("빈 테이블을 설정할 수 있다.")
	@Test
	void setEmptyFalse() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("empty", true);

		given().log().all()
			.pathParam("orderTableId", 2)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/tables/{orderTableId}/empty")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.assertThat().body("empty", equalTo(true));
	}

	@DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
	@Test
	void changeEmpty_WhenTableAssignedToGroup() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("empty", false);

		given().log().all()
			.pathParam("orderTableId", 8)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/tables/{orderTableId}/empty")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("주문 상태가 조리인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
	@Test
	void changeEmpty_WhenStatusIsCooking() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("empty", false);

		given().log().all()
			.pathParam("orderTableId", 6)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/tables/{orderTableId}/empty")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("주문 상태가 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
	@Test
	void changeEmpty_WhenStatusIsMeal() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("empty", false);

		given().log().all()
			.pathParam("orderTableId", 7)
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.put("/api/tables/{orderTableId}/empty")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("방문한 손님 수를 입력할 수 있다.")
	@Test
	void changeNumberOfGuests() {
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("numberOfGuests", 5);

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.pathParam("orderTableId", 2)
			.when()
			.put("/api/tables/{orderTableId}/number-of-guests")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.assertThat().body("numberOfGuests", equalTo(5));
	}
}
