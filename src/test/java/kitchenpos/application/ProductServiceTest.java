package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateResponse;
import kitchenpos.dto.ProductFindResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {
    @Test
    @DisplayName("상품을 생성한다")
    void create() {
        final Product product = saveAndGetProduct(1L);

        final ProductCreateResponse actual = productService.create(product.getName(), product.getPrice());

        assertThat(actual.getName()).isEqualTo("하와이안피자");
    }

    @Test
    @DisplayName("상품 전체를 조회한다")
    void list() {
        saveAndGetProduct(1L);

        final List<ProductFindResponse> actual = productService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual)
                        .extracting("name")
                        .containsExactly("하와이안피자")
        );
    }
}
