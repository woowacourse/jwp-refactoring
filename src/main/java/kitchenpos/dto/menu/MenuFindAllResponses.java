package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;

public class MenuFindAllResponses {
	private List<MenuFindAllResponse> menuFindAllResponses;

	protected MenuFindAllResponses() {
	}

	public MenuFindAllResponses(List<MenuFindAllResponse> menuFindAllResponses) {
		this.menuFindAllResponses = menuFindAllResponses;
	}

	public static MenuFindAllResponses from(List<Menu> menus) {
		return menus.stream()
			.map(MenuFindAllResponse::new)
			.collect(Collectors.collectingAndThen(Collectors.toList(), MenuFindAllResponses::new));
	}

	public List<MenuFindAllResponse> getMenuFindAllResponses() {
		return menuFindAllResponses;
	}
}
