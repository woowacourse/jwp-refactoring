package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(
            final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts
    ) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
        validateMenuPrice(price, menuProducts);

        final Menu savedMenu = menuDao.save(new Menu(name, price, menuGroupId));
        updateMenuProductsByMenuId(menuProducts, savedMenu);

        return savedMenu;
    }

    private void validateMenuPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void updateMenuProductsByMenuId(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        savedMenu.addMenuIdToMenuProducts();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProductDao.update(menuProduct);
        }
        savedMenu.changeAllMenuProducts(new MenuProducts(menuProducts));
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
