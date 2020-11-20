package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateResponse {
	private Long seq;
	private Long menuId;
	private Long productId;
	private long quantity;

	protected MenuProductCreateResponse() {
	}

	public MenuProductCreateResponse(Long seq, Long menuId, Long productId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public MenuProductCreateResponse(MenuProduct menuProduct) {
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
}
