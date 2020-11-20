package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {
	private Long seq;
	private Long menuId;
	private Long productId;
	private Long quantity;

	protected MenuProductCreateRequest() {
	}

	public MenuProductCreateRequest(Long seq, Long menuId, Long productId, Long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public MenuProductCreateRequest(MenuProduct menuProduct) {
		this(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
	}

	public Long getSeq() {
		return seq;
	}

	public Long getMenuId() {
		return menuId;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}

	public MenuProduct toEntity() {
		return new MenuProduct(seq, menuId, productId, quantity);
	}
}
