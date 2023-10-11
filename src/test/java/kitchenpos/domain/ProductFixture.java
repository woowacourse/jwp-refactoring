package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 상품_생성() {
        Product product = new Product();
        product.setId(null);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(20000));
        return product;
    }

}