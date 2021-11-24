package kitchenpos;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductInformationRequest;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .name("상품이름")
                .price(BigDecimal.valueOf(1000))
                .build();
    }

    public static Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .name("상품이름")
                .price(BigDecimal.valueOf(1000))
                .build();
    }

    public static Product createProduct(Long id, String name, Long price) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(BigDecimal.valueOf(price))
                .build();
    }

    public static Product updateProduct(Product product, ProductInformationRequest request) {
        return Product.builder()
                .id(product.getId())
                .name(request.getName())
                .price(BigDecimal.valueOf(request.getPrice()))
                .build();
    }

}
