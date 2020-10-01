package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/truncate.sql")
@SpringBootTest
class JdbcTemplateProductDaoTest {
    private static final String DELETE_PRODUCT = "delete from product where id in (:ids)";
    private static final int BIG_DECIMAL_FLOOR_SCALE = 2;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    private List<Long> productIds;

    @BeforeEach
    void setUp() {
        productIds = new ArrayList<>();
    }

    @DisplayName("상품 저장")
    @Test
    void saveTest() {
        Product product = createProduct("후라이드치킨", BigDecimal.valueOf(16000));

        Product savedProduct = jdbcTemplateProductDao.save(product);
        productIds.add(savedProduct.getId());

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(
                        product.getPrice().setScale(BIG_DECIMAL_FLOOR_SCALE, BigDecimal.ROUND_FLOOR))
        );
    }

    @DisplayName("아이디에 맞는 상품 반환")
    @Test
    void findByIdTest() {
        Product product = createProduct("후라이드치킨", BigDecimal.valueOf(16000));
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
        Product firstProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16000));
        Product secondProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16000));
        Product thirdProduct = createProduct("후라이드치킨", BigDecimal.valueOf(16000));
        jdbcTemplateProductDao.save(firstProduct);
        jdbcTemplateProductDao.save(secondProduct);
        jdbcTemplateProductDao.save(thirdProduct);

        List<Product> allProducts = jdbcTemplateProductDao.findAll();
        allProducts.forEach(product -> productIds.add(product.getId()));

        assertThat(allProducts).hasSize(3);
    }

    private Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    @AfterEach
    void tearDown() {
        Map<String, Object> params = Collections.singletonMap("ids", productIds);
        namedParameterJdbcTemplate.update(DELETE_PRODUCT, params);
    }
}