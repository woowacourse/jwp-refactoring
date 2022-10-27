package kitchenpos.dao;

import static kitchenpos.support.ProductFixtures.PRODUCT1;
import static kitchenpos.support.ProductFixtures.createAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(JdbcTemplateProductDao.class)
public class JdbcTemplateProductDaoTest {

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @DisplayName("product를 저장한다.")
    @Test
    void save() {
        // given
        final Product product = PRODUCT1.createWithNullId();

        // when
        final Product savedProduct = jdbcTemplateProductDao.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("product 하나를 조회한다.")
    @Test
    void findById() {
        // given
        final Product expected = PRODUCT1.create();

        // when
        final Optional<Product> product = jdbcTemplateProductDao.findById(1L);

        // then
        assertAll(
                () -> assertThat(product.isPresent()).isTrue(),
                () -> assertThat(product.get()).usingRecursiveComparison().ignoringFields("price").isEqualTo(expected)
        );
    }

    @DisplayName("product들을 조회한다.")
    @Test
    void findAll() {
        // given
        final List<Product> expected = createAll();

        // when
        final List<Product> products = jdbcTemplateProductDao.findAll();

        // then
        assertThat(products).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .isEqualTo(expected);
    }
}
