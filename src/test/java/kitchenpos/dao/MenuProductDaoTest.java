package kitchenpos.dao;

import static kitchenpos.support.fixtures.DomainFixtures.MENU1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.MENU1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT1_PRICE;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_NAME;
import static kitchenpos.support.fixtures.DomainFixtures.PRODUCT2_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductDaoTest extends DaoTest {

    private MenuProductDao menuProductDao;
    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        menuDao = new JdbcTemplateMenuDao(dataSource);
        menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        productDao = new JdbcTemplateProductDao(dataSource);
    }

    @Test
    @DisplayName("MenuProduct를 저장한다.")
    void save() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
        Product product = productDao.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));

        MenuProduct menuProduct = menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), 1));

        assertThat(menuProduct).isEqualTo(menuProductDao.findById(menuProduct.getSeq()).orElseThrow());
    }

    @Test
    @DisplayName("모든 MenuProduct를 조회한다.")
    void findAll() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
        Product product = productDao.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));

        MenuProduct menuProduct = menuProductDao.save(new MenuProduct(menu.getId(), product.getId(), 1));

        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertAll(
                () -> assertThat(menuProducts).isNotEmpty(),
                () -> assertThat(menuProducts).contains(menuProduct)
        );
    }

    @Test
    @DisplayName("Menu에 속한 모든 MenuProduct를 조회한다.")
    void findAllByMenuId() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(MENU_GROUP_NAME1));
        Menu menu = menuDao.save(new Menu(MENU1_NAME, MENU1_PRICE, menuGroup.getId()));
        Product product1 = productDao.save(new Product(PRODUCT1_NAME, PRODUCT1_PRICE));
        Product product2 = productDao.save(new Product(PRODUCT2_NAME, PRODUCT2_PRICE));

        MenuProduct menuProduct1 = menuProductDao.save(new MenuProduct(menu.getId(), product1.getId(), 1));
        MenuProduct menuProduct2 = menuProductDao.save(new MenuProduct(menu.getId(), product2.getId(), 3));

        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());

        assertThat(menuProducts).containsExactly(menuProduct1, menuProduct2);
    }
}
