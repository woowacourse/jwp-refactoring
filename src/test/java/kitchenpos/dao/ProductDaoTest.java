package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class ProductDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(jdbcTemplate.getJdbcTemplate().getDataSource());
    }

    @Test
    void 상품을_저장한다() {
        // given
        Product product = new Product();
        product.setName("product");
        product.setPrice(new BigDecimal("1000"));

        // when
        Product save = productDao.save(product);

        // then
        assertThat(save.getId()).isNotNull();
    }

    @Test
    void 아이디로_상품을_조회한다() {
        // given
        Product expected = new Product();
        expected.setId(1L);
        expected.setName("후라이드");
        expected.setPrice(new BigDecimal("16000"));

        // when
        Product result = productDao.findById(expected.getId())
                .orElseThrow();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(expected.getName());
            softly.assertThat(result.getPrice()).isEqualByComparingTo(expected.getPrice());
        });
    }

    @Test
    void 상품을_전체_조회한다() {
        // given & when
        List<Product> result = productDao.findAll();

        // then
        assertThat(result).hasSizeGreaterThan(1);
    }
}
