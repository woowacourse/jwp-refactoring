package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.TestFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Transactional
    @DisplayName("새로운 Product를 추가할 수 있다.")
    @Test
    void createProductTest() {
        Product product = createProduct("까르보나라치킨", BigDecimal.valueOf(18_000));

        Product savedProduct = productService.create(product);

        assertAll(() -> {
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getId()).isNotNegative();
            assertThat(savedProduct.getName()).isEqualTo(product.getName());
            assertThat(savedProduct.getPrice()).isEqualByComparingTo(product.getPrice());
        });
    }

    @DisplayName("예외: 이름이 null인 Product를 추가")
    @Test
    void createProductWithoutNameTest() {
        Product product = createProduct(null, BigDecimal.valueOf(18_000));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외: 가격이 0보다 작은 Product를 추가")
    @Test
    void createProductWithoutPriceTest() {
        Product product = createProduct("까르보나라치킨", BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외: 가격이 null인 Product를 추가")
    @Test
    void createProductWithNegativePriceTest() {
        Product product = createProduct("까르보나라치킨", null);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("모든 Product를 조회할 수 있다.")
    @Test
    void findAllProductsTest() {
        Product friedChicken = createProduct("후라이드", BigDecimal.valueOf(16_000));
        Product seasonedChicken = createProduct("양념치킨", BigDecimal.valueOf(17_000));
        Product halfAndHalfChicken = createProduct("반반치킨", BigDecimal.valueOf(18_000));
        productDao.save(friedChicken);
        productDao.save(seasonedChicken);
        productDao.save(halfAndHalfChicken);

        List<Product> products = productService.list();

        assertAll(() -> {
            assertThat(products).hasSize(3);
            assertThat(products).extracting(Product::getName).containsOnly(friedChicken.getName(), seasonedChicken.getName(), halfAndHalfChicken.getName());
        });
    }
}