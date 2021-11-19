package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ProductServiceTest extends IntegrationTest {

    @DisplayName("상품을 생성한다")
    @Test
    void create() {
        // given
        Product product = ProductFixture.productForCreate("product", 1000L);

        // when
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("product");
        assertThat(savedProduct.getPrice().longValue()).isEqualTo(1000L);
    }

    @DisplayName("상품의 이름은 null일 수 없다")
    @Test
    void create_fail_productNameCannotBeNull() {
        // given
        Product product = ProductFixture.productForCreate(null, 1000L);

        // when, then
        assertThatCode(() -> productService.create(product))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("상품의 가격은 null일 수 없다")
    @Test
    void create_fail_productPriceCannotBeNull() {
        // given
        Product product = ProductFixture.productForCreate("product", (BigDecimal) null);

        // when, then
        assertThatCode(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격은 음수일 수 없다")
    @Test
    void create_fail_productPriceCannotBeNegative() {
        // given
        Product product = ProductFixture.productForCreate("product", -1L);

        // when, then
        assertThatCode(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격은 0 이상이어야 한다")
    @ParameterizedTest
    @ValueSource(longs = {0L, 1L})
    void create_success_productPriceIsZeroOrPositive(Long price) {
        // given
        Product product = ProductFixture.productForCreate("product", price);

        // when, then
        assertThatCode(() -> productService.create(product))
                .doesNotThrowAnyException();
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}