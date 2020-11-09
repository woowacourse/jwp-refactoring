package kitchenpos.acceptance.menugroup;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceStep {

	public static MenuGroup createMenuGroup(String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);

		return menuGroup;
	}

	public static ExtractableResponse<Response> requestToCreateMenuGroup(MenuGroup menuGroup) {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuGroup)
			.when()
			.post("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static void assertThatCreateMenuGroup(ExtractableResponse<Response> response, MenuGroup expected) {
		MenuGroup actual = response.jsonPath().getObject(".", MenuGroup.class);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName())
		);
	}

	public static ExtractableResponse<Response> requestToFindMenuGroups() {
		return given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.get("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static void assertThatFindMenuGroups(ExtractableResponse<Response> response, List<MenuGroup> expected) {
		List<MenuGroup> actual = response.jsonPath().getList(".", MenuGroup.class);

		assertThat(actual).usingElementComparatorOnFields("id").isNotNull();
		assertThat(actual).usingElementComparatorOnFields("name").isEqualTo(expected);
	}
}
