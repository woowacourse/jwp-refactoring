package kitchenpos.application;

import static kitchenpos.fixture.ProductFixtures.상품_목록_조회;
import static kitchenpos.fixture.ProductFixtures.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ProductServiceTest {

    @Autowired
    private ProductService sut;

    @Nested
    @DisplayName("상품 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 상품을 등록할 수 있다.")
        @ParameterizedTest(name = "[{index}] {displayName} price = {0}")
        @ValueSource(longs = {0, 1_000})
        void createProduct(final Long price) {
            final Product product = new Product("짱구", BigDecimal.valueOf(price));

            final Product actual = sut.create(product);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(product.getName()),
                    () -> assertThat(actual.getPrice().longValue()).isEqualTo(price)
            );
        }

        @DisplayName("상품 가격이 없는 경우 등록할 수 없다.")
        @Test
        void createWithNullPrice() {
            final Product 짱구 = 상품_생성("짱구", null);

            assertThatThrownBy(() -> sut.create(짱구))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 가격이 0원보다 적은 경우 등록할 수 없다.")
        @Test
        void createWithPriceLessThanZero() {
            final Product 짱구 = 상품_생성("짱구", BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> sut.create(짱구))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProducts() {
        final List<Product> products = 상품_목록_조회();

        assertThat(sut.list())
                .hasSize(6)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .isEqualTo(products);
    }
}
