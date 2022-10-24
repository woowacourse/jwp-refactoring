package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
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

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 생성한다.")
    @Test
    void create_success() {
        // given
        Product product = new Product("강정치킨", new BigDecimal("17000"));

        // when
        Product savedProduct = productService.create(product);

        // then
        Product dbProduct = productDao.findById(savedProduct.getId())
                .orElseThrow();
        assertThat(dbProduct.getName()).isEqualTo(product.getName());
    }

    @DisplayName("상품을 생성할 때 가격이 null이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_null() {
        // given
        Product product = new Product("강정치킨", null);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 생성할 때 가격이 음수이면 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        // given
        Product product = new Product("강정치킨", new BigDecimal("-1"));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list_success() {
        // given
        Product product = new Product("강정치킨", new BigDecimal("17000"));
        productService.create(product);

        // when
        List<Product> products = productService.list();

        // then
        List<String> productNames = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        assertThat(productNames).contains(product.getName());
    }
}
