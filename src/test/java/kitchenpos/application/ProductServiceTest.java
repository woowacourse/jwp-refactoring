package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 저장한다.")
    void create() {
        final Product product1 = new Product();
        product1.setPrice(new BigDecimal(1000));
        product1.setName("상품1");
        final Product product2 = new Product();
        product2.setPrice(new BigDecimal(1000));
        product2.setName("상품2");
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final List<Product> list = productService.list();

        final List<Long> savedIds = list.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(savedIds).contains(savedProduct1.getId(), savedProduct2.getId());
    }

    @Test
    @DisplayName("상품의 가격은 null일 수 없다.")
    void createWithNullPrice() {
        final Product product = new Product();
        product.setName("상품");
        product.setPrice(null);
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격은 0보다 작을 수 없다.")
    void createWithUnderZeroPrice() {
        final Product product = new Product();
        product.setName("상품");
        product.setPrice(new BigDecimal(-1));
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 이름은 null일 수 없다.")
    void createWithNullName() {
        final Product product1 = new Product();
        product1.setPrice(new BigDecimal(1000));
        assertThatThrownBy(() ->productService.create(product1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}