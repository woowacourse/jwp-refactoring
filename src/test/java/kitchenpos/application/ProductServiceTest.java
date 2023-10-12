package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 상품_등록_성공_저장() {
        // given
        final Product product = new Product();
        product.setName("상품");
        product.setPrice(new BigDecimal("1000"));

        // when
        final Product saved = productService.create(product);

        // then
        assertThat(productService.list())
                .map(Product::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }

    @Test
    @DisplayName("상품 등록 시 가격 정보가 있어야 한다.")
    void 상품_등록_실패_가격_없음() {
        // given
        final Product product = new Product();
        product.setName("상품");

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 등록 시 가격은 0 이상의 수여야 한다.")
    void 상품_등록_실패_가격_음수() {
        // given
        final Product product = new Product();
        product.setName("상품");
        product.setPrice(new BigDecimal("-1000"));

        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
