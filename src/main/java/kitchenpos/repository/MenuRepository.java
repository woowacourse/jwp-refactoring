package kitchenpos.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.exception.NotFoundMenuGroupException;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(MenuDao menuDao, MenuGroupDao menuGroupDao, MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(Menu menu, List<MenuProduct> menuProducts) {
        validateMenuGroupId(menu.getMenuGroupId());
        Menu savedMenu = menuDao.save(menu);
        List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menuProducts);
        return new Menu(savedMenu, savedMenuProducts);
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
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
