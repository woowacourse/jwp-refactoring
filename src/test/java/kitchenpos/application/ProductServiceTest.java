package kitchenpos.application;

import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.application.dto.request.ProductCommand;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidProductException;
import kitchenpos.support.cleaner.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Product를 생성할 때 ")
    class CreateTest {

        @ParameterizedTest
        @ValueSource(longs = {-1, -18000})
        @DisplayName("Product 가격이 음수이면 실패한다.")
        void productPriceNegativeFailed(long price) {
            assertThatThrownBy(
                    () -> productService.create(new ProductCommand(PRODUCT1_NAME, BigDecimal.valueOf(price))))
                    .isInstanceOf(InvalidProductException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 18000})
        @DisplayName("Product를 생성한다.")
        void create(long price) {
            ProductResponse productResponse = productService.create(
                    new ProductCommand(PRODUCT1_NAME, BigDecimal.valueOf(price)));

            assertAll(
                    () -> assertThat(productResponse.id()).isNotNull(),
                    () -> assertThat(productResponse.name()).isEqualTo(PRODUCT1_NAME),
                    () -> assertThat(productResponse.price()).isEqualTo(BigDecimal.valueOf(price))
            );
        }
    }

    @Test
    @DisplayName("Product를 모두 조회한다.")
    void list() {
        productRepository.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        productRepository.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        assertThat(productService.list()).hasSize(2);
    }
}
