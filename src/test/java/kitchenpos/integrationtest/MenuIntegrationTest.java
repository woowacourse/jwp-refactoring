package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

@DisplayName("Menu 통합 테스트")
public class MenuIntegrationTest extends IntegrationTest {

	@DisplayName("1개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
	@Test
	void create() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "35000");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.assertThat().body("name", equalTo("맛있는 치킨 세트"))
			.assertThat().body("price", equalTo(35000f))
			.assertThat().body("menuGroupId", equalTo(1))
			.assertThat().body("menuProducts", hasSize(2));
	}

	@DisplayName("메뉴의 가격은 0 원 이상이어야 한다.")
	@Test
	void create_WhenPriceLessThanZero() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "-1");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("메뉴의 가격은 메뉴에 속한 상품 금액의 합보다 같거나 작아야 한다.")
	@Test
	void create_WhenPriceBiggerThanPriceSum() {
		Map<String, String> oneFriedChicken = new HashMap<>();
		oneFriedChicken.put("productId", "1");
		oneFriedChicken.put("quantity", "1");

		Map<String, String> twoSourceChicken = new HashMap<>();
		twoSourceChicken.put("productId", "2");
		twoSourceChicken.put("quantity", "2");

		List<Map<String, String>> menuProducts = Arrays.asList(
			oneFriedChicken,
			twoSourceChicken
		);

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 치킨 세트");
		requestBody.put("price", "49000");
		requestBody.put("menuGroupId", "1");
		requestBody.put("menuProducts", menuProducts);

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
