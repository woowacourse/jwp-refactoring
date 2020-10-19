package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Transactional
    @DisplayName("새로운 Product를 추가할 수 있다.")
    @Test
    void createProduct() {
        Product product = new Product();
        product.setName("까르보나라치킨");
        product.setPrice(BigDecimal.valueOf(18_000));

        Product savedProduct = productService.create(product);

        assertAll(() -> {
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getId()).isNotNegative();
            assertThat(savedProduct.getName()).isEqualTo(product.getName());
            assertThat(savedProduct.getPrice()).isEqualByComparingTo(product.getPrice());
        });
    }

    @DisplayName("예외 테스트: 이름이 null인 Product를 추가하면 예외가 발생한다.")
    @Test
    void createProductWithoutName() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(18_000));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외 테스트: 가격이 null인 Product를 추가하면 예외가 발생한다.")
    @Test
    void createProductWithoutPrice() {
        Product product = new Product();
        product.setName("까르보나라치킨");
        product.setPrice(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: 가격이 0보다 작은 Product를 추가하면 예외가 발생한다.")
    @Test
    void createProductWithNegativePrice() {
        Product product = new Product();
        product.setName("까르보나라치킨");

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Product를 조회할 수 있다.")
    @Test
    void findAllProducts() {
        String[] productNames = {"후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"};

        List<Product> products = productService.list();

        assertAll(() -> {
            assertThat(products).hasSize(6);
            assertThat(products).extracting(Product::getName).containsOnly(productNames);
        });
    }
}