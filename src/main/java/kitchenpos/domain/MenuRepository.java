package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao menuDao, final JdbcTemplateMenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final Menu menuEntity = menuDao.save(menu);

        return MenuFactory.create(menuEntity, toMenuProductEntities(menuEntity.getId(), menuProducts));
    }

    private List<MenuProduct> toMenuProductEntities(final Long menuId, final List<MenuProduct> menuProducts) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct menuProductWithMenuId = new MenuProduct(
                    null,
                    menuId,
                    menuProduct.getProductId(),
                    menuProduct.getPrice(),
                    menuProduct.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProductWithMenuId));
        }

        return savedMenuProducts;
    }

    public Menu getById(final Long id) {
        final Menu menuEntity = menuDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProductEntities = menuProductDao.findAllByMenuId(id);

        return MenuFactory.create(menuEntity, menuProductEntities);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = new ArrayList<>();

        final List<Menu> menuEntities = menuDao.findAll();
        for (final Menu menuEntity : menuEntities) {
            final List<MenuProduct> menuProductEntities = menuProductDao.findAllByMenuId(menuEntity.getId());
            menus.add(MenuFactory.create(menuEntity, menuProductEntities));
        }

        return menus;
    }

    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
