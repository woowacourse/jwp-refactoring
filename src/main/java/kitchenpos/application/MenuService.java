package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuService(final MenuDao menuDao, final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Transactional
    public Menu create(String name, Long price, MenuGroup menuGroup, Map<Product, Integer> productQuantity) {
        Menu menu = menuDao.save(Menu.of(name, price, menuGroup, productQuantity));
        setMenuProductInMenu(menu, productQuantity);
        return menu;
    }

    private void setMenuProductInMenu(Menu menu, Map<Product, Integer> productQuantity) {
        Long menuId = menu.getId();
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : productQuantity.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            MenuProduct menuProduct = menuProductDao.save(new MenuProduct(menuId, product.getId(), quantity));
            menuProducts.add(menuProduct);
        }
        menu.setMenuProducts(menuProducts);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }
}
