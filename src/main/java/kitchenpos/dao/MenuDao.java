package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;

public interface MenuDao {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}

@Repository
class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    private final JdbcTemplateMenuDao jdbcTemplateMenuDao;
    private final JdbcTemplateProductDao jdbcTemplateProductDao;
    private final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    public MenuRepository(final JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao,
                          final JdbcTemplateMenuDao jdbcTemplateMenuDao,
                          final JdbcTemplateProductDao jdbcTemplateProductDao,
                          final JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao) {
        this.jdbcTemplateMenuProductDao = jdbcTemplateMenuProductDao;
        this.jdbcTemplateMenuDao = jdbcTemplateMenuDao;
        this.jdbcTemplateProductDao = jdbcTemplateProductDao;
        this.jdbcTemplateMenuGroupDao = jdbcTemplateMenuGroupDao;
    }

    @Override
    public Menu save(final Menu entity) {
        if (!jdbcTemplateMenuGroupDao.existsById(entity.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu savedMenu = jdbcTemplateMenuDao.save(entity);
        final List<MenuProduct> menuProducts = entity.getMenuProducts();
        for (final MenuProduct menuProduct : menuProducts) {
            setPrice(menuProduct);
            menuProduct.setMenuId(savedMenu.getId());
            jdbcTemplateMenuProductDao.save(menuProduct);
        }
        savedMenu.setMenuProducts(menuProducts);
        return savedMenu;
    }

    private void setPrice(final MenuProduct menuProduct) {
        final Optional<Product> product = jdbcTemplateProductDao.findById(menuProduct.getProductId());
        product.ifPresent(it -> menuProduct.setPrice(it.getPrice()));
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        final List<MenuProduct> menuProducts = jdbcTemplateMenuProductDao.findAllByMenuId(id);
        for (final MenuProduct menuProduct : menuProducts) {
            setPrice(menuProduct);
        }
        final Optional<Menu> menu = jdbcTemplateMenuDao.findById(id);
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
