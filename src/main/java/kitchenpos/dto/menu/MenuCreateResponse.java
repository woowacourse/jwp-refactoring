package kitchenpos.dto.menu;

import kitchenpos.domain.Menu;

public class MenuCreateResponse {
	private Long id;
	private String name;
	private Long price;
	private Long menuGroupId;
	private MenuProductCreateResponses menuProducts;

	protected MenuCreateResponse() {
	}

	public MenuCreateResponse(Long id, String name, Long price, Long menuGroupId,
		MenuProductCreateResponses menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public MenuCreateResponse(Menu savedMenu) {
		this(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice().getValue(), savedMenu.getMenuGroupId(),
			MenuProductCreateResponses.from(savedMenu.getMenuProducts()));
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

	public MenuProductCreateResponses getMenuProducts() {
		return menuProducts;
	}
}
