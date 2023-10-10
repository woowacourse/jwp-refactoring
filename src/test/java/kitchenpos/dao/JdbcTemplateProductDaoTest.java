package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateProductDao.class)
@JdbcTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @Test
    void 저장한다() {
        // given
        Product product = new Product();
        product.setName("테스트");
        product.setPrice(BigDecimal.valueOf(10000));

        // when
        Product result = jdbcTemplateProductDao.save(product);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getName()).isEqualTo(product.getName());
            softly.assertThat(result.getPrice().longValue()).isEqualTo(product.getPrice().longValue());
        });
    }

    @Test
    void id로_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<Product> result = jdbcTemplateProductDao.findById(id);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getId()).isEqualTo(id);
        });
    }

    @Test
    void 모두_조회한다() {
        // when
        List<Product> result = jdbcTemplateProductDao.findAll();

        // then
        assertThat(result).isNotEmpty();
    }
}
