package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProducts;

public class MenuProductFindAllResponses {
	private List<MenuProductFindAllResponse> menuProductFindAllResponses;

	protected MenuProductFindAllResponses() {
	}

	public MenuProductFindAllResponses(List<MenuProductFindAllResponse> menuProductFindAllResponses) {
		this.menuProductFindAllResponses = menuProductFindAllResponses;
	}

	public static MenuProductFindAllResponses from(MenuProducts menuProducts) {
		return menuProducts.getMenuProducts()
			.stream()
			.map(MenuProductFindAllResponse::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), MenuProductFindAllResponses::new));
	}

	public List<MenuProductFindAllResponse> getMenuProductFindAllResponses() {
		return menuProductFindAllResponses;
	}
}
