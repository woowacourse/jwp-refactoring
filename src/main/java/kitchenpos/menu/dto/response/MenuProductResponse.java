package kitchenpos.menu.dto.response;

import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private ProductResponse product;
    private long quantity;

    private MenuProductResponse(ProductResponse product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                ProductResponse.from(menuProduct.getProduct()),
                menuProduct.getQuantity()
        );
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
