package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@DaoTest
class JdbcTemplateMenuProductDaoTest {

    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateProductDao jdbcTemplateProductDao;
    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    public JdbcTemplateMenuProductDaoTest(
            final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
            final JdbcTemplateMenuDao jdbcTemplateMenuDao,
            final JdbcTemplateProductDao jdbcTemplateProductDao,
            final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao
    ) {
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateProductDao = jdbcTemplateProductDao;
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
    }

    @Test
    void save_menu_product() {
        // given
        final MenuProduct menuProduct = menuProductFixtureFrom(3L);

        // when
        final MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);

        // then
        assertThat(savedMenuProduct.getSeq()).isNotNull();
    }


    @Test
    void find_by_seq() {
        // given
        final MenuProduct menuProduct = menuProductFixtureFrom(3L);
        final MenuProduct savedMenuProduct = jdbcTemplateMenuProductDao.save(menuProduct);
        final Long savedMenuProductSeq = savedMenuProduct.getSeq();

        // when
        final Optional<MenuProduct> menuProductById = jdbcTemplateMenuProductDao.findById(savedMenuProductSeq);

        // then
        assertThat(menuProductById).isPresent();
        assertThat(menuProductById.get().getSeq()).isEqualTo(savedMenuProductSeq);
    }

    @Test
    void find_by_seq_return_empty_when_result_doesnt_exist() {
        // given
        final long doesntExistId = 10000L;

        // when
        final Optional<MenuProduct> menuProductById = jdbcTemplateMenuProductDao.findById(doesntExistId);

        // then
        assertThat(menuProductById).isEmpty();
    }

    @Test
    void find_all() {
        // given
        jdbcTemplateMenuProductDao.save(menuProductFixtureFrom(3L));
        jdbcTemplateMenuProductDao.save(menuProductFixtureFrom(2L));

        // when
        final List<MenuProduct> findAll = jdbcTemplateMenuProductDao.findAll();

        // then
        assertThat(findAll).hasSize(2);
    }

    @Test
    void find_all_by_menu_id() {
        // given
        final MenuProduct savedMenuProduct1 = jdbcTemplateMenuProductDao.save(menuProductFixtureFrom(3L));
        final MenuProduct savedMenuProduct2 = jdbcTemplateMenuProductDao.save(menuProductFixtureFrom(2L));
        final Long findMenuId = savedMenuProduct2.getMenuId();

        // when
        final List<MenuProduct> findAll = jdbcTemplateMenuProductDao.findAllByMenuId(findMenuId);

        // then
        assertThat(findAll).hasSize(1);
        assertThat(findAll.get(0).getSeq()).isEqualTo(savedMenuProduct2.getSeq());
    }


    private MenuProduct menuProductFixtureFrom(final Long quantity) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(quantity);
        menuProduct.setMenuId(getMenuFixtureIdOf("fried-chicken", "Chicken-group"));
        menuProduct.setProductId(getProductFixtureIdFrom("chicken"));
        return menuProduct;
    }

    private Long getMenuFixtureIdOf(final String name, final String menuGroupName) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(22000L));
        menu.setMenuGroupId(getMenuGroupFixtureIdFrom(menuGroupName));
        return jdbcTemplateMenuDao.save(menu).getId();
    }

    private Long getMenuGroupFixtureIdFrom(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return jdbcTemplateMenuGroupDao.save(menuGroup).getId();
    }

    private Long getProductFixtureIdFrom(final String name) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(10000L));
        return jdbcTemplateProductDao.save(product).getId();
    }
}
