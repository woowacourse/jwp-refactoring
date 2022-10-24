package kitchenpos.dao;

import static kitchenpos.support.ProductFixtures.PRODUCT1;
import static org.assertj.core.api.Assertions.assertThat;

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
}
