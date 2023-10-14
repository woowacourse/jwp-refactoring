package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateProductDaoTest {

    private final JdbcTemplateProductDao jdbcTemplateProductDao;

    public JdbcTemplateProductDaoTest(final JdbcTemplateProductDao jdbcTemplateProductDao) {
        this.jdbcTemplateProductDao = jdbcTemplateProductDao;
    }

    @Test
    void save_product() {
        // given
        final Product product = productFixtureFrom("chicken");

        // when
        final Product savedProduct = jdbcTemplateProductDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }


    @Test
    void find_by_id() {
        // given
        final Product product = productFixtureFrom("chicken");
        final Product savedProduct = jdbcTemplateProductDao.save(product);
        final Long savedProductId = savedProduct.getId();

        // when
        final Optional<Product> productById = jdbcTemplateProductDao.findById(savedProductId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(productById).isPresent();
            softly.assertThat(productById.get())
                    .usingRecursiveComparison()
                    .isEqualTo(savedProduct);
        });
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<Product> productById = jdbcTemplateProductDao.findById(doesntExistId);

        // then
        assertThat(productById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateProductDao.save(productFixtureFrom("chicken"));
        jdbcTemplateProductDao.save(productFixtureFrom("radish"));

        // when
        final List<Product> findAll = jdbcTemplateProductDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }


    private Product productFixtureFrom(final String name) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(1000));
        return product;
    }
}
