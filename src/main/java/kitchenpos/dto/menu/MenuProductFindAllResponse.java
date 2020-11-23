package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

public class MenuProductFindAllResponse {
	private Long seq;
	private Long menuId;
	private Long productId;
	private long quantity;

	protected MenuProductFindAllResponse() {
	}

	public MenuProductFindAllResponse(Long seq, Long menuId, Long productId, long quantity) {
		this.seq = seq;
		this.menuId = menuId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public MenuProductFindAllResponse(MenuProduct menuProduct) {
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
