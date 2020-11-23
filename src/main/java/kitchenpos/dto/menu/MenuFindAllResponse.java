package kitchenpos.dto.menu;

import kitchenpos.domain.Menu;

public class MenuFindAllResponse {
	private Long id;
	private String name;
	private Long price;
	private Long menuGroupId;
	private MenuProductFindAllResponses menuProducts;

	protected MenuFindAllResponse() {
	}

	public MenuFindAllResponse(Long id, String name, Long price, Long menuGroupId,
		MenuProductFindAllResponses menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public MenuFindAllResponse(Menu menu) {
		this(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroupId(),
			MenuProductFindAllResponses.from(menu.getMenuProducts()));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public MenuProductFindAllResponses getMenuProducts() {
		return menuProducts;
	}
}
