package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProducts;

public class MenuProductCreateResponses {
	private List<MenuProductCreateResponse> menuProductCreateResponses;

	protected MenuProductCreateResponses() {
	}

	public MenuProductCreateResponses(List<MenuProductCreateResponse> menuProductCreateResponses) {
		this.menuProductCreateResponses = menuProductCreateResponses;
	}

	public static MenuProductCreateResponses from(MenuProducts menuProducts) {
		List<MenuProductCreateResponse> menuProductCreateResponses = menuProducts.getMenuProducts()
			.stream()
			.map(MenuProductCreateResponse::new)
			.collect(Collectors.toList());

		return new MenuProductCreateResponses(menuProductCreateResponses);
	}

	public List<MenuProductCreateResponse> getMenuProductCreateResponses() {
		return menuProductCreateResponses;
	}
}
