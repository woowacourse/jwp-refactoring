package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class JdbcTemplateProductDaoTest {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplateProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("제품을 저장한다.")
    void save() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();

        // when
        final Product saved = productDao.save(product);

        // then
        assertThat(saved).extracting("id")
                .isNotNull();
    }

    @Test
    @DisplayName("제품을 조회한다.")
    void findById() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();
        final Product saved = productDao.save(product);

        // when
        final Product actual = productDao.findById(saved.getId())
                .orElseThrow(RuntimeException::new);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("저장된 모든 제품을 조회한다.")
    void findAll() {
        // given
        final Product product = ProductFixture.createDefaultWithoutId();
        final Product saved = productDao.save(product);

        // when
        final List<Product> actual = productDao.findAll();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .contains(saved);
    }
}
