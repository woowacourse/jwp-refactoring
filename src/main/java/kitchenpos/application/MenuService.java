package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(final MenuDao menuDao, final MenuProductDao menuProductDao,
                       MenuGroupDao menuGroupDao, ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        menuGroupDao.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
        updateMenuProductOfPrice(menuProducts);

        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
        return menuDao.save(menu);
    }

    private void updateMenuProductOfPrice(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProduct.updatePrice(product.getPrice());
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }
}
