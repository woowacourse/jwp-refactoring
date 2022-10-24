package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        final String productName = "맥북m1";
        final BigDecimal productPrice = BigDecimal.valueOf(3000);
        final Product product = new Product(productName, productPrice);

        final Product persistedProduct = productService.create(product);

        assertAll(
                () -> assertThat(persistedProduct.getName()).isEqualTo(productName),
                () -> assertThat(persistedProduct.getPrice()).isEqualByComparingTo(productPrice)
        );
    }

    @Test
    @DisplayName("상품 가격이 null일 경우 예외 발생")
    void whenProductPriceIsNull() {
        final Product product = new Product("맥북m1", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격이 0원 일때 생성한다.")
    void whenProductPriceIsZero() {
        final String productName = "맥북m1";
        final BigDecimal productPrice = BigDecimal.ZERO;
        final Product product = new Product(productName, productPrice);

        final Product persistedProduct = productService.create(product);

        assertAll(
                () -> assertThat(persistedProduct.getName()).isEqualTo(productName),
                () -> assertThat(persistedProduct.getPrice()).isEqualByComparingTo(productPrice)
        );
    }

    @Test
    @DisplayName("상품 가격이 0원 미만 일때 예외 발생")
    void whenProductPriceIsUnderZero() {
        final Product product = new Product("맥북m1", BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getProducts() {
        final List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}
