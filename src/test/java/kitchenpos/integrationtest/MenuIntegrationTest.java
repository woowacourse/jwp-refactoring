package kitchenpos.integrationtest;

import static io.restassured.RestAssured.*;
import static kitchenpos.integrationtest.step.MenuIntegrationTestStep.*;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;
import kitchenpos.integrationtest.common.IntegrationTest;

@DisplayName("Menu 통합 테스트")
public class MenuIntegrationTest extends IntegrationTest {

	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	@Test
	void list() {
		given().log().all()
			.accept(ContentType.JSON)
			.when()
			.get("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.body(".", hasSize(6));
	}

	@DisplayName("1개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.")
	@Test
	void create() {
		Map<String, Object> requestBody = createValidMenu();

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.body("name", equalTo("맛있는 치킨 세트"))
			.body("price", equalTo(35000f))
			.body("menuGroupId", equalTo(1))
			.body("menuProducts", hasSize(2));
	}

	@DisplayName("메뉴의 가격은 0 원 이상이어야 한다.")
	@Test
	void create_WhenPriceLessThanZero() {
		Map<String, Object> requestBody = createMenuThatPriceIsLessThanZero();

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
	void create_WhenPriceBiggerThanSum() {
		Map<String, Object> requestBody = createMenuThatPriceIsBiggerThanSum();

		given().log().all()
			.body(requestBody)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.when()
			.post("/api/menus")
			.then().log().all()
			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@DisplayName("메뉴는 특정 메뉴 그룹에 속해야 한다.")
	@Test
	void create_WhenNotBelongToAnyMenuGroup() {
		Map<String, Object> requestBody = createMenuThatNotBelongToAnyGroup();

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
