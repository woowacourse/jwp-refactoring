package kitchenpos.acceptance.menu;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceStep;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptanceStep;
import kitchenpos.acceptance.product.ProductAcceptanceStep;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuAcceptanceTest extends AcceptanceTest {

	@DisplayName("메뉴를 생성한다")
	@Test
	void create() {
		// given
		Product product = ProductAcceptanceStep.getPersist("방어회", 24_000);
		MenuGroup menuGroup = MenuGroupAcceptanceStep.getPersist("겨울 회");

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(1L);

		Menu menu = MenuAcceptanceStep.create("방어회 (소)", 24_000, menuGroup, menuProduct);

		// when
		ExtractableResponse<Response> response = MenuAcceptanceStep.requestToCreateMenuGroup(menu);

		// then
		AcceptanceStep.assertThatStatusIsCreated(response);
		MenuAcceptanceStep.assertThatCreateMenu(response, menu);
	}

	@DisplayName("모든 메뉴를 조회한다")
	@Test
	void list() {
		// given
		Product product = ProductAcceptanceStep.getPersist("방어회", 32_000);
		MenuGroup menuGroup = MenuGroupAcceptanceStep.getPersist("겨울 회");

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(product.getId());
		menuProduct.setQuantity(1L);

		Menu menu = MenuAcceptanceStep.create("방어회 (소)", 24_000, menuGroup, menuProduct);
		Menu menu2 = MenuAcceptanceStep.create("방어회 (중)", 32_000, menuGroup, menuProduct);

		MenuAcceptanceStep.requestToCreateMenuGroup(menu);
		MenuAcceptanceStep.requestToCreateMenuGroup(menu2);

		// when
		ExtractableResponse<Response> response = MenuAcceptanceStep.requestToFindMenus();

		// then
		AcceptanceStep.assertThatStatusIsOk(response);
		MenuAcceptanceStep.assertThatFindMenus(response, Arrays.asList(menu, menu2));
	}
}
