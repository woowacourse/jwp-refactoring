package kitchenpos.fixture;

import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product createProductEntity(Long id, String name, long price) {
        return Product.builder()
                .name(name)
                .price(price)
                .build();
    }
}
