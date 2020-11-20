package kitchenpos.dto.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Money;

public class MenuCreateRequest {
	private Long id;
	private String name;
	private Long price;
	private Long menuGroupId;
	private MenuProductCreateRequests menuProducts;

	protected MenuCreateRequest() {
	}

	public MenuCreateRequest(Long id, String name, Long price, Long menuGroupId,
		MenuProductCreateRequests menuProducts) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
		this.menuProducts = menuProducts;
	}

	public MenuCreateRequest(Menu menu) {
		this(menu.getId(), menu.getName(), menu.getPrice().getValue(), menu.getMenuGroupId(),
			MenuProductCreateRequests.from(menu.getMenuProducts()));
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

	public MenuProductCreateRequests getMenuProducts() {
		return menuProducts;
	}

	public Menu toEntity() {
		return new Menu(id, name, new Money(price), menuGroupId, menuProducts.toMenuProducts());
	}
}
