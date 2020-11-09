package kitchenpos.acceptance.menugroup;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

	@DisplayName("메뉴 그룹을 생성한다")
	@Test
	void create() {
		// given
		MenuGroup menuGroup = MenuGroupAcceptanceStep.create("겨울 회");

		// when
		ExtractableResponse<Response> response = MenuGroupAcceptanceStep.requestToCreateMenuGroup(menuGroup);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		MenuGroupAcceptanceStep.assertThatCreateMenuGroup(response, menuGroup);
	}

	@DisplayName("모든 메뉴 그룹을 조회한다")
	@Test
	void list() {
		// given
		MenuGroup menuGroup = MenuGroupAcceptanceStep.create("겨울 회");
		MenuGroup menuGroup2 = MenuGroupAcceptanceStep.create("해산물");

		MenuGroupAcceptanceStep.requestToCreateMenuGroup(menuGroup);
		MenuGroupAcceptanceStep.requestToCreateMenuGroup(menuGroup2);

		// when
		ExtractableResponse<Response> response = MenuGroupAcceptanceStep.requestToFindMenuGroups();

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		MenuGroupAcceptanceStep.assertThatFindMenuGroups(response, Arrays.asList(menuGroup, menuGroup2));
	}
}
