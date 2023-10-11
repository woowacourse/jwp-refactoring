package kitchenpos.dao;

import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_REQUEST;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_MENU_ID;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_PRODUCT_ID;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_QUANTITY;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import javax.sql.DataSource;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class JdbcTemplateMenuProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private MenuProductDao menuProductDao;

    @BeforeEach
    void setUp() {
        this.menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
    }

    @Test
    @DisplayName("MenuProduct를 영속화한다.")
    void saveMenuProduct() {
        // given
        final MenuProduct menuProduct = MENU_PRODUCT1_REQUEST();

        // when
        MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenuProduct.getSeq()).isNotNull();
            softly.assertThat(savedMenuProduct.getMenuId()).isEqualTo(MENU_PRODUCT1_MENU_ID);
            softly.assertThat(savedMenuProduct.getProductId()).isEqualTo(MENU_PRODUCT1_PRODUCT_ID);
            softly.assertThat(savedMenuProduct.getQuantity()).isEqualTo(MENU_PRODUCT1_QUANTITY);
        });
    }
}
