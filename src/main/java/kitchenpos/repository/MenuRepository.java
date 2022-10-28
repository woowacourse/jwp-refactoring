package kitchenpos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(MenuDao menuDao, MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(Menu menu) {
        Menu savedMenu = menuDao.save(menu);

        Long menuId = savedMenu.getId();
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            MenuProduct toSaveProduct = new MenuProduct(menuId, menuProduct.getProductId(),
                    menuProduct.getQuantity());
            savedMenu.addMenuProduct(menuProductDao.save(toSaveProduct));
        }
        return savedMenu;
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();

        for (Menu menu : menus) {
            addAllMenuProducts(menu);
        }

        return menus;
    }

    private void addAllMenuProducts(Menu menu) {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
        for (MenuProduct menuProduct : menuProducts) {
            menu.addMenuProduct(menuProduct);
        }
    }
}
