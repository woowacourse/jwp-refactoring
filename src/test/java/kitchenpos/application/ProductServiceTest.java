package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 시 전달받은 정보를 새 id로 저장한다.")
    void 상품_등록_성공_저장() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);

        // when
        final Product saved = productService.create(product);

        // then
        assertThat(productService.list())
                .map(Product::getId)
                .filteredOn(id -> Objects.equals(id, saved.getId()))
                .hasSize(1);
    }
}
