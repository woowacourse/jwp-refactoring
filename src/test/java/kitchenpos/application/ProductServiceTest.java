package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.ProductDao;
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

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("상품 등록")
    class CreateTest {

        @DisplayName("정상적인 경우 상품을 등록할 수 있다.")
        @ParameterizedTest(name = "[{index}] {displayName} price = {0}")
        @ValueSource(longs = {0, 1_000_000_000})
        void createProduct(final Long price) {
            final Product product = new Product();
            product.setName("짱구");
            product.setPrice(BigDecimal.valueOf(price));

            final Product actual = sut.create(product);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getName()).isEqualTo(product.getName()),
                    () -> assertThat(actual.getPrice()).isNotNull()
            );
        }

        @DisplayName("상품 가격이 없는 경우 등록할 수 없다.")
        @Test
        void createWithNullPrice() {
            final Product product = new Product();
            product.setName("짱구");

            assertThatThrownBy(() -> sut.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 가격이 0원보다 적은 경우 등록할 수 없다.")
        @Test
        void createWithPriceLessThanZero() {
            final Product product = new Product();
            product.setName("짱구");
            product.setPrice(BigDecimal.valueOf(-1));

            assertThatThrownBy(() -> sut.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProducts() {
        final Product product = new Product();
        product.setName("짱구");
        product.setPrice(BigDecimal.TEN);

        final Product product1 = new Product();
        product1.setName("짱아");
        product1.setPrice(BigDecimal.TEN);

        sut.create(product);
        sut.create(product1);

        assertThat(sut.list())
                .hasSize(2)
                .extracting("name", "price")
                .containsExactly(
                        tuple(product.getName(), product.getPrice()),
                        tuple(product1.getName(), product1.getPrice())
                );
    }
}