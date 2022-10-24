package kitchenpos.support.fixture.domain;

import kitchenpos.domain.MenuProduct;

public enum MenuProductFixture {

    ONE(1L),
    TWO(2L),
    ;

    private final Long quantity;

    MenuProductFixture(Long quantity) {
        this.quantity = quantity;
    }

    public MenuProduct getMenuProduct(Long menuId, Long productId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public MenuProduct getMenuProduct(Long seq, Long menuId, Long productId) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
