package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("상품 테스트")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        // given
        final int newProductId = productService.list().size() + 1;
        final Product product = new Product();
        product.setName("test");
        product.setPrice(BigDecimal.valueOf(100));

        // when
        final Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isEqualTo(newProductId);
    }

    @DisplayName("상품 가격이 음수일 시 실패한다")
    @Test
    void create_FailPrice() {
        // given
        final Product product = new Product();
        product.setName("test");
        product.setPrice(BigDecimal.valueOf(-1));

        // when
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() {
        // given & when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
