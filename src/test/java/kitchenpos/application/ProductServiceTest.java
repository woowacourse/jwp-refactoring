package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 저장한다.")
    void create() {
        final Product product1 = 상품_등록("상품1", 1000L);
        final Product product2 = 상품_등록("상품2", 1000L);

        final List<Product> products = 상품_전체_조회();

        assertThat(products).usingElementComparatorIgnoringFields()
                .contains(product1, product2);
    }

    @Test
    @DisplayName("상품의 가격은 null일 수 없다.")
    void createWithNullPrice() {
        assertThatThrownBy(() -> 상품_등록("상품", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격은 0보다 작을 수 없다.")
    void createWithUnderZeroPrice() {
        assertThatThrownBy(() -> 상품_등록("상품", -1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 이름은 null일 수 없다.")
    void createWithNullName() {
        assertThatThrownBy(() -> 상품_등록(null, 1000L))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
