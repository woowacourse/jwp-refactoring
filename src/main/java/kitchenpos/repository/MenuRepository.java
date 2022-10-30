package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Component;

@Component
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(MenuDao menuDao, MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(Menu menu, List<MenuProduct> menuProducts) {
        Menu savedMenu = menuDao.save(menu);
        List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menuProducts);
        return new Menu(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(Long menuId, List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductDao.save(new MenuProduct(menuId, menuProduct)))
                .collect(Collectors.toList());
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> new Menu(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
