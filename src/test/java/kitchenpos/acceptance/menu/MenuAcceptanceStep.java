package kitchenpos.acceptance.menu;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuAcceptanceStep {

	public static Menu create(String name, int price, MenuGroup menuGroup, MenuProduct menuProduct) {
		Menu menu = new Menu();
		menu.setName(name);
		menu.setPrice(BigDecimal.valueOf(price));
		menu.setMenuGroupId(menuGroup.getId());
		menu.setMenuProducts(Collections.singletonList(menuProduct));
		return menu;
	}

	public static ExtractableResponse<Response> requestToCreateMenuGroup(Menu menu) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menu)
			.when()
			.post("/api/menus")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateMenu(ExtractableResponse<Response> response, Menu expected) {
		Menu actual = response.jsonPath().getObject(".", Menu.class);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName()),
			() -> assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice()),
			() -> assertThat(actual.getMenuGroupId()).isNotNull(),
			() -> assertThat(actual.getMenuProducts()).usingElementComparatorOnFields("productId")
				.isEqualTo(expected.getMenuProducts()),
			() -> assertThat(actual.getMenuProducts()).usingElementComparatorOnFields("menuId").isNotNull()
		);
	}

	public static ExtractableResponse<Response> requestToFindMenus() {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/menus")
			.then().log().all()
			.extract();
	}

	public static void assertThatFindMenus(ExtractableResponse<Response> response, List<Menu> expected) {
		List<Menu> actual = response.jsonPath().getList(".", Menu.class);

		assertAll(
			() -> assertThat(actual).usingElementComparatorOnFields("id").isNotNull(),
			() -> assertThat(actual).usingElementComparatorOnFields("name").isEqualTo(expected)
		);
	}

	public static Menu getPersist(String name, int price, MenuGroup menuGroup, MenuProduct menuProduct) {
		Menu menu = create(name, price, menuGroup, menuProduct);
		ExtractableResponse<Response> response = requestToCreateMenuGroup(menu);
		return response.jsonPath().getObject(".", Menu.class);
	}
}
