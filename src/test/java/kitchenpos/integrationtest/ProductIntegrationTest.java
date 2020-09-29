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

public class ProductIntegrationTest extends IntegrationTest {

	@DisplayName("상품을 등록할 수 있다.")
	@Test
	void create() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨");
		requestBody.put("price", "16000");

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/products")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.assertThat().body("id", notNullValue())
			.assertThat().body("name", equalTo("맛있는 치킨"))
			.assertThat().body("price", equalTo(16000f));
	}

	@DisplayName("상품의 가격은 0원 이상이어야 한다.")
	@Test
	void create_WhenParamIsNotPositive() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨");
		requestBody.put("price", "-1");

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/products")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("상품의 목록을 조회할 수 있다")
	@Test
	void list() {
		given().log().all()
			.accept(ContentType.JSON)
			.when()
			.get("/api/products")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.assertThat().body(".", hasSize(6));
	}
}
