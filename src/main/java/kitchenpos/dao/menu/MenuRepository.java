package kitchenpos.dao.menu;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.menuproduct.JdbcTemplateMenuProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Menu;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository implements MenuDao {

    private final JdbcTemplateMenuDao menuDao;
    private final JdbcTemplateMenuProductDao menuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao menuDao, final JdbcTemplateMenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final Menu menu = menuDao.save(entity);
        final Long menuId = menu.getId();
        final LinkedList<MenuProduct> savedMenuProducts = new LinkedList<>();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.changeMenu(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        entity.addMenuProducts(savedMenuProducts);
        return menuDao.save(entity);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
