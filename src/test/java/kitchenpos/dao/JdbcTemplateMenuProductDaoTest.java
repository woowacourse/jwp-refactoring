package kitchenpos.dao;

import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_MENU_ID;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_PRODUCT_ID;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_QUANTITY;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT1_REQUEST;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT2_MENU_ID;
import static kitchenpos.common.MenuProductFixtures.MENU_PRODUCT2_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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

    @Test
    @DisplayName("메뉴 ID에 해당하는 모든 메뉴 상품을 조회한다.")
    void findAllByMenuId() {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("truncate table menu_product");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        final MenuProduct menuProduct1 = MENU_PRODUCT1_REQUEST();
        final MenuProduct menuProduct2 = MENU_PRODUCT2_REQUEST();
        final MenuProduct savedMenuProduct1 = menuProductDao.save(menuProduct1);
        final MenuProduct savedMenuProduct2 = menuProductDao.save(menuProduct2);
        List<MenuProduct> expected = List.of(savedMenuProduct1, savedMenuProduct2);

        // when
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(MENU_PRODUCT2_MENU_ID);

        // then
        assertThat(menuProducts).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expected);
    }
}
