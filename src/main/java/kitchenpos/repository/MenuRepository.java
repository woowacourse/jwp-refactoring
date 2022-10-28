package kitchenpos.repository;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final MenuDao menuDao,
                          final MenuGroupDao menuGroupDao,
                          final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        final Long menuId = savedMenu.getId();

        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.addMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    public boolean isGroupExist(final Menu menu) {
        return menuGroupDao.existsById(menu.getMenuGroupId());
    }
}
