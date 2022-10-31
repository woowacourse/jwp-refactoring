package kitchenpos.support.fixture.domain;

import kitchenpos.menu.domain.MenuProduct;

public enum MenuProductFixture {

    ONE(1L),
    TWO(2L),
    ;

    private final Long quantity;

    MenuProductFixture(Long quantity) {
        this.quantity = quantity;
    }

    public MenuProduct getMenuProduct(Long productId) {
        return new MenuProduct(null, productId, quantity);
    }

    public MenuProduct getMenuProduct(Long menuId, Long productId) {
        return new MenuProduct(menuId, productId, quantity);
    }

    public MenuProduct getMenuProduct(Long id, Long menuId, Long productId) {
        return new MenuProduct(id, menuId, productId, quantity);
    }
}
