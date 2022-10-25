package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @DisplayName("create 메서드는 상품 가격이 null이면 예외를 발생시킨다.")
    @Test
    void create_null_price_throwException() {
        // given
        BigDecimal price = null;
        Product request = createProductRequest(price);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 상품 가격이 음수면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -10_000L})
    void create_invalid_price_throwException(Long input) {
        // given
        BigDecimal price = BigDecimal.valueOf(input);
        Product request = createProductRequest(price);

        // when & then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
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
