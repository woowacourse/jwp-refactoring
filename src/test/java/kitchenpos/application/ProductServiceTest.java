package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        final String name = "마이쮸";
        final BigDecimal price = BigDecimal.valueOf(800);

        final Product product = new Product(null, name, price);

        final Product savedProduct = productService.create(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(name),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(price)
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        final List<Product> products = productService.list();

        assertThat(products).hasSize(6);
    }
}
