package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.Fixtures;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    ProductService productService;

    @DisplayName("특정 메뉴 상품을 추가할 시 메뉴 상품 목록에 추가된다.")
    @Test
    void createAndList() {
        Product 후라이드 = Fixtures.상품_후라이드();

        Product saved = productService.create(후라이드);

        assertThat(productService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }

    @DisplayName("상품 가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        assertThatThrownBy(() -> new Product(1L, "후라이드", BigDecimal.valueOf(-10000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 0 이상이어야 한다.");
    }
}
