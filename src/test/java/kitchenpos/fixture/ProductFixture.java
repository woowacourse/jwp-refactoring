package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateDto;

public class ProductFixture {

    public static final Product 후라이드_16000 = 상품("후라이드", 16000L);
    public static final Product 양념치킨_16000 = 상품("양념치킨", 16000L);
    public static final Product 순살치킨_16000 = 상품("순살치없킨", 16000L);

    public static ProductCreateDto 상품_생성_요청(String name, int price) {
        return new ProductCreateDto(name, BigDecimal.valueOf(price));
    }

    public static Product 상품(String name, Long price) {
        return new Product(name, BigDecimal.valueOf(price));
    }
}
