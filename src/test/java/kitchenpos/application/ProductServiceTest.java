package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest extends ServiceTest {

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        // given
        Product request = createProduct(null, "후라이드", BigDecimal.valueOf(16000));

        // when
        Product actual = productService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("후라이드")
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        saveAndGetProduct();

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
