package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao, MenuGroupDao menuGroupDao,
                       MenuProductDao menuProductDao, ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(Menu menu) {
        validateIsNotExistMenu(menu);
        validateProductAndPrice(menu);

        return saveMenu(menu);
    }

    private void validateIsNotExistMenu(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProductAndPrice(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        menu.validatePriceIsCheaperThanSum(sum);
    }

    private Menu saveMenu(Menu menu) {
        Menu savedMenu = menuDao.save(menu);

        Long menuId = savedMenu.getId();
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);
            savedMenu.addMenuProduct(savedMenuProduct);
        }
        return savedMenu;
    }

    public List<Menu> list() {
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
