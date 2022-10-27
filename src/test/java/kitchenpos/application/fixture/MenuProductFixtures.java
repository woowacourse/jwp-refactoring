package kitchenpos.application.fixture;

import kitchenpos.domain.MenuProduct;

public class MenuProductFixtures {

    public static final MenuProduct generateMenuProduct(final Long productId, final long quantity) {
        return generateMenuProduct(null, null, productId, quantity);
    }

    public static final MenuProduct generateMenuProduct(final Long seq, final MenuProduct menuProduct) {
        return generateMenuProduct(seq, menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static final MenuProduct generateMenuProduct(final Long seq,
                                                        final Long menuId,
                                                        final Long productId,
                                                        final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
