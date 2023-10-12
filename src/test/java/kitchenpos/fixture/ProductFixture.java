package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

@SuppressWarnings("NonAsciiCharacters")
public class ProductFixture {

    public static Product 상품_엔티티_A = createProductEntity(1L, "gray", BigDecimal.valueOf(1_000));
    public static Product 상품_엔티티_B_가격_NULL = createProductEntity(3L, "candy", null);
    public static Product 상품_엔티티_C_가격_음수 = createProductEntity(4L, "apple", BigDecimal.valueOf(-1_000));

    private static Product createProductEntity(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
