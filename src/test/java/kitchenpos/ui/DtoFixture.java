package kitchenpos.ui;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.Product;

public class DtoFixture {

    public static ProductDto getProduct() {
        final Product product = new Product(1L, "productName", BigDecimal.valueOf(1000L));
        return ProductDto.from(product);
    }
}
