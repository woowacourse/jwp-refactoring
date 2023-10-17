package kitchenpos.fixture;

import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_엔티티_A = createProductEntity(1L, "gray", 1_000);
    public static Product 상품_엔티티_B_가격_NULL = createProductEntity(3L, "candy", 0);
    public static Product 상품_엔티티_C_가격_음수 = createProductEntity(4L, "apple", -1_000);

    private static Product createProductEntity(Long id, String name, long price) {
        return Product.builder()
                .name(name)
                .price(price)
                .build();
    }
}
