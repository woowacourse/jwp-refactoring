package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.DomainFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JdbcTemplateProductDaoTest extends JdbcTemplateDaoTest {
    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @BeforeEach
    void setUp() {
        productIds = new ArrayList<>();
    }

    @DisplayName("상품 저장")
    @Test
    void saveTest() {
        Product product = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));

        Product savedProduct = jdbcTemplateProductDao.save(product);
        productIds.add(savedProduct.getId());

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(
                        product.getPrice().setScale(BIG_DECIMAL_FLOOR_SCALE, BigDecimal.ROUND_FLOOR))
        );
    }

    @DisplayName("아이디에 맞는 상품 반환")
    @Test
    void findByIdTest() {
        Product product = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));
        Product savedProduct = jdbcTemplateProductDao.save(product);

        Product findProduct = jdbcTemplateProductDao.findById(savedProduct.getId()).get();
        productIds.add(findProduct.getId());

        assertAll(
                () -> assertThat(findProduct.getId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(findProduct.getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(findProduct.getPrice()).isEqualTo(savedProduct.getPrice())
        );
    }

    @DisplayName("잘못된 아이디 입력 시 빈 객체 반환")
    @Test
    void findByIdWithInvalidIdTest() {
        Optional<Product> findProduct = jdbcTemplateProductDao.findById(0L);

        assertThat(findProduct).isEqualTo(Optional.empty());
    }

    @DisplayName("모든 상품 반환")
    @Test
    void findAllTest() {
        Product firstProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16_000));
        Product secondProduct = createProduct("양념치킨", BigDecimal.valueOf(16_000));
        Product thirdProduct = createProduct("간장치킨", BigDecimal.valueOf(16_000));
        jdbcTemplateProductDao.save(firstProduct);
        jdbcTemplateProductDao.save(secondProduct);
        jdbcTemplateProductDao.save(thirdProduct);

        List<Product> allProducts = jdbcTemplateProductDao.findAll();
        allProducts.forEach(product -> productIds.add(product.getId()));

        assertThat(allProducts).hasSize(3);
    }

    @AfterEach
    void tearDown() {
        deleteProduct();
    }
}