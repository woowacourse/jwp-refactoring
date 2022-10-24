package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JdbcTemplateMenuProductDaoTest extends JdbcTemplateTest{

    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    @DisplayName("데이터 베이스에 저장할 경우 id 값을 가진 엔티티로 반환한다.")
    void save() {
        final MenuProduct savedMenuProduct = menuProductDao.save(getMenuProduct());
        assertThat(savedMenuProduct.getSeq()).isNotNull();
    }
}
