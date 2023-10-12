package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
        final Product product = productFixtureOf("chicken");

        // when
        final Product savedProduct = jdbcTemplateProductDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }


    @Test
    void find_by_id() {
        // given
        final Product product = productFixtureOf("chicken");
        final Product savedProduct = jdbcTemplateProductDao.save(product);
        final Long savedMenuGroupId = savedProduct.getId();

        // when
        Optional<Product> menuGroupDaoById = jdbcTemplateProductDao.findById(savedMenuGroupId);

        // then
        assertThat(menuGroupDaoById).isPresent();
        assertThat(menuGroupDaoById.get().getId()).isEqualTo(savedMenuGroupId);
    }

    @Test
    void find_by_id_return_empty_when_result_doesnt_exist() {
        // given
        long doesntExistId = 10000L;

        // when
        Optional<Product> menuGroupDaoById = jdbcTemplateProductDao.findById(doesntExistId);

        // then
        assertThat(menuGroupDaoById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateProductDao.save(productFixtureOf("chicken"));
        jdbcTemplateProductDao.save(productFixtureOf("radish"));

        // when
        final List<Product> findAll = jdbcTemplateProductDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }


    private Product productFixtureOf(final String name) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(1000));
        return product;
    }
}
