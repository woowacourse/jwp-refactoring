package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.response.ProductResponse;

public class MenuProductResponse {
    private final Long seq;
    private final ProductResponse product;
    private final long quantity;

    private MenuProductResponse(final Long seq, final ProductResponse product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.seq(),
                ProductResponse.from(menuProduct.product()),
                menuProduct.quantity()
        );
    }

    public Long seq() {
        return seq;
    }

    public ProductResponse product() {
        return product;
    }

    public long quantity() {
        return quantity;
    }
}
