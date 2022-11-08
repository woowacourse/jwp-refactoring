package kitchenpos.menu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menugroup.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.dao.JdbcTemplateProductDao;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Repository;

@Repository
class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    private final JdbcTemplateProductDao jdbcTemplateProductDao;

    public MenuRepository(final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                          final JdbcTemplateMenuDao jdbcTemplateMenuDao,
                          final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao,
                          final JdbcTemplateProductDao jdbcTemplateProductDao) {
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
        this.jdbcTemplateProductDao = jdbcTemplateProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Menu savedMenu = jdbcTemplateMenuDao.save(entity);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.setMenuId(savedMenu.getId());
            savedMenuProducts.add(jdbcTemplateMenuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        final Optional<Menu> menu = jdbcTemplateMenuDao.findById(id);
        final MenuProducts menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(id);
        menu.ifPresent(it -> it.setMenuProducts(menuProducts.getMenuProducts()));
        return menu;
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = jdbcTemplateMenuDao.findAll();
        for (final Menu menu : menus) {
            final MenuProducts menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId());
            menu.setMenuProducts(menuProducts.getMenuProducts());
        }
        return menus;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return jdbcTemplateMenuDao.countByIdIn(ids);
    }

    @Override
    public Menus findAllByIdIn(final List<Long> ids) {
        final List<Menu> menus = new ArrayList<>();
        for (final Long id : ids) {
            final Menu menu = jdbcTemplateMenuDao.findById(id).get();
            final MenuProducts menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(id);
            final Products products = jdbcTemplateProductDao.findAllByIdIn(menuProducts.getProductIds());
            menuProducts.setProducts(products);
            menu.setMenuProducts(menuProducts.getMenuProducts());
            final MenuGroup menuGroup = jdbcTemplateMenuGroupDao.findById(menu.getMenuGroupId()).get();
            menu.setMenuGroup(menuGroup);
            menus.add(menu);
        }
        return new Menus(menus);
    }
}
