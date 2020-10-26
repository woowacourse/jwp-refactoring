package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(value = "/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        Product product = new Product("상품", 10000L);

        Product savedProduct = productService.create(product);

        Product findProduct = productDao.findById(savedProduct.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(findProduct.getName()).isEqualTo(product.getName());
        assertThat(findProduct.getPrice()).isEqualByComparingTo(product.getPrice());
    }

    @DisplayName("상품 가격이 양수가 아니면 생성할 수 없다.")
    @Test
    void createException1() {
        Product product = new Product("상품", -100L);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 이름이 없으면 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("wrongNameProvider")
    void createException2(String name) {
        Product product = new Product(name, 10000L);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s : 올바르지 않은 이름입니다.", name);
    }

    private static Stream<String> wrongNameProvider() {
        return Stream.of("", null);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        Product product1 = new Product("상품1", 10000L);
        Product product2 = new Product("상품2", 15000L);
        Product product3 = new Product("상품3", 20000L);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);

        List<Product> products = productService.list();

        assertThat(products).hasSize(3);
    }

    @AfterEach
    void tearDown() {
        productDao.deleteAll();
    }
}