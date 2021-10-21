package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

public class JdbcTemplateProductDaoTest extends DaoTest {

    private JdbcTemplateProductDao jdbcTemplateProductDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @DisplayName("Product 저장")
    @Test
    void save() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));
        Product savedProduct = jdbcTemplateProductDao.save(product);

        assertThat(jdbcTemplate.queryForObject("select count(1) from product", Integer.class)).isEqualTo(7);
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct)
            .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(product);
    }

    @DisplayName("Product 조회")
    @Test
    void findById() {
        Product expected = new Product();
        expected.setId(3L);
        expected.setName("반반치킨");
        expected.setPrice(BigDecimal.valueOf(16000));

        assertThat(jdbcTemplateProductDao.findById(expected.getId())).isPresent()
            .get()
            .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("Product 전체 조회")
    @Test
    void findAll() {
        assertThat(jdbcTemplateProductDao.findAll()).hasSize(6);
    }
}
