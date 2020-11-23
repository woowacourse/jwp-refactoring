package kitchenpos.dto.menu;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProducts;

public class MenuProductCreateRequests {
	private List<MenuProductCreateRequest> menuProductCreateRequests;

	protected MenuProductCreateRequests() {
	}

	public MenuProductCreateRequests(List<MenuProductCreateRequest> menuProductCreateRequests) {
		this.menuProductCreateRequests = menuProductCreateRequests;
	}

	public static MenuProductCreateRequests from(MenuProducts menuProducts) {
		List<MenuProductCreateRequest> menuProductCreateRequests = menuProducts.getMenuProducts()
			.stream()
			.map(MenuProductCreateRequest::new)
			.collect(Collectors.toList());

		return new MenuProductCreateRequests(menuProductCreateRequests);
	}

	public MenuProducts toMenuProducts() {
		return menuProductCreateRequests.stream()
			.map(MenuProductCreateRequest::toEntity)
			.collect(Collectors.collectingAndThen(Collectors.toList(), MenuProducts::new));
	}

	public List<MenuProductCreateRequest> getMenuProductCreateRequests() {
		return menuProductCreateRequests;
	}
}
