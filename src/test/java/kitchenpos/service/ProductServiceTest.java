package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class ProductServiceTest {

    private final ProductService productService;

    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("상품의 가격은")
    @Nested
    class PriceIs {

        @Test
        @DisplayName("값이 존재하지 않는다면 예외가 발생한다.")
        public void createWithNotContainPrice() {
            Product product = new Product();

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("값이 음수라면 예외가 발생한다.")
        public void createWithNegativePrice() {
            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(-1L));

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
