package kitchenpos.application;

import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.ProductFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.support.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("Product를 생성할 때 ")
    class CreateTest {

        @ParameterizedTest
        @ValueSource(longs = {-1, -18000})
        @DisplayName("Product 가격이 음수이면 실패한다.")
        void productPriceNegativeFailed(long price) {
            assertThatThrownBy(() -> productService.create(new Product(PRODUCT1_NAME, BigDecimal.valueOf(price))))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {0, 18000})
        @DisplayName("Product를 생성한다.")
        void create(long price) {
            Product product = productService.create(new Product(PRODUCT1_NAME, BigDecimal.valueOf(price)));

            assertThat(product).isEqualTo(productDao.findById(product.getId()).orElseThrow());
        }
    }

    @Test
    @DisplayName("Product를 모두 조회한다.")
    void findAll() {
        Product product = productService.create(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));

        assertThat(productService.list()).contains(product);
    }
}
