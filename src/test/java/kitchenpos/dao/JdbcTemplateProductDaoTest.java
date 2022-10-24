package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateProductDaoTest extends JdbcTemplateTest{

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final Product savedProduct = productDao.save(후라이드());
        assertThat(savedProduct.getId()).isNotNull();
    }
}
