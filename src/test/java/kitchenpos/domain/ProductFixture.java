package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 치킨_생성() {
        Product product = new Product();
        product.setId(null);
        product.setName("치킨");
        product.setPrice(BigDecimal.valueOf(20000));
        return product;
    }

    public static Product 피자_생성() {
        Product product = new Product();
        product.setId(null);
        product.setName("피자");
        product.setPrice(BigDecimal.valueOf(23000));
        return product;
    }

}