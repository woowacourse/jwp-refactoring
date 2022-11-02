package kitchenpos.menu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;

    public MenuRepository(final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                          final JdbcTemplateMenuDao jdbcTemplateMenuDao) {
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
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
        final List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(id);
        menu.ifPresent(it -> it.setMenuProducts(menuProducts));
        return menu;
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = jdbcTemplateMenuDao.findAll();
        for (final Menu menu : menus) {
            final List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(menu.getId());
            menu.setMenuProducts(menuProducts);
        }
        return menus;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return jdbcTemplateMenuDao.countByIdIn(ids);
    }
}
