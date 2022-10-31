package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {

        Menu menu = menuProduct.getMenu();
        Product product = menuProduct.getProduct();

        return new MenuProductResponse(menuProduct.getSeq(), menu.getId(), product.getId(), menuProduct.getQuantity());
    }
}
