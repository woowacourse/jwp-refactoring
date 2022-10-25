package kitchenpos.application;

import static kitchenpos.Fixtures.상품_후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @DisplayName("특정 메뉴 상품을 추가할 시 메뉴 상품 목록에 추가된다.")
    @Test
    void createAndList() {
        Product 상품_후라이드 = productService.create(상품_후라이드());

        List<Product> 상품들 = productService.list();

        assertThat(상품들)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(상품_후라이드);
    }

    @DisplayName("상품 가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        assertThatThrownBy(() -> new Product(1L, "후라이드", BigDecimal.valueOf(-10000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0 이상이어야 한다.");
    }
}
