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

@DisplayName("MenuGroup 통합 테스트")
public class MenuGroupIntegrationTest extends IntegrationTest {

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("name", "맛있는 메뉴");

		given().log().all()
			.body(requestBody)
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.when()
			.post("/api/menu-groups")
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.body("id", notNullValue())
			.body("name", equalTo("맛있는 메뉴"));
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	void list() {
		given().log().all()
			.accept(ContentType.JSON)
			.when()
			.get("/api/menu-groups")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.body(".", hasSize(4));
	}
}
