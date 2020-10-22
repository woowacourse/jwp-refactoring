package kitchenpos.factory;

import org.springframework.stereotype.Component;

import kitchenpos.domain.MenuProduct;

@Component
public class MenuProductFactory {
    public MenuProduct create(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public MenuProduct create(Long productId, long quantity) {
        return create(null, null, productId, quantity);
    }

    public MenuProduct create(Long menuId, Long productId, long quantity) {
        return create(null, menuId, productId, quantity);
    }
}
