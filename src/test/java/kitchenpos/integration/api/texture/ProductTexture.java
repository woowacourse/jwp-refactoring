package kitchenpos.integration.api.texture;

import java.math.BigDecimal;
import kitchenpos.product.ui.request.ProductCreateRequest;

public enum ProductTexture {
    강정치킨(ProductCreateRequest.create("강정치킨", new BigDecimal(17000))),
    민초치킨(ProductCreateRequest.create("민초치킨", new BigDecimal(18000)));

    private final ProductCreateRequest product;

    ProductTexture(ProductCreateRequest product) {
        this.product = product;
    }

    public ProductCreateRequest getProduct() {
        return product;
    }
}
