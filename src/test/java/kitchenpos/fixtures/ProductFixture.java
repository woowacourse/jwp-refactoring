package kitchenpos.fixtures;

import kitchenpos.domain.Product;

import java.math.BigDecimal;


public class ProductFixture {

    public static Product 맛있는뿌링클() {
        Product product = new Product();
        product.setId(1L);
        product.setName("맛있는뿌링클");
        product.setPrice(new BigDecimal(19000));
        return product;
    }

    public static Product 알리오갈리오() {
        Product product = new Product();
        product.setId(2L);
        product.setName("알리오갈리오");
        product.setPrice(new BigDecimal(10000));
        return product;
    }

    public static Product 쫀득쫀득치즈볼() {
        Product product = new Product();
        product.setId(3L);
        product.setName("쫀득쫀득치즈볼");
        product.setPrice(new BigDecimal(5000));
        return product;
    }

    public static Product 시원한콜라() {
        Product product = new Product();
        product.setId(4L);
        product.setName("시원한콜라");
        product.setPrice(new BigDecimal(2000));
        return product;
    }

    public static Product 치킨무() {
        Product product = new Product();
        product.setId(5L);
        product.setName("치킨무");
        product.setPrice(new BigDecimal(500));
        return product;
    }

    public static Product 아메리카노() {
        Product product = new Product();
        product.setId(6L);
        product.setName("아메리카노");
        product.setPrice(new BigDecimal(3500));
        return product;
    }
}
