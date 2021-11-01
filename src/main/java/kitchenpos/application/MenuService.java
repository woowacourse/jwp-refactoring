package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Menu create(final Menu menu) {
        checkValidMenu(menu);

        return saveMenu(menu);
    }

    private Menu saveMenu(Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenuId(savedMenu.getId());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private void checkValidMenu(Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final BigDecimal price = menu.getPrice();
        if (isInvalidPrice(price)) {
            throw new IllegalArgumentException();
        }

        if (isNotGrouppingMenu(menu)) {
            throw new IllegalArgumentException();
        }
        checkInvalidSumPrice(price, menuProducts);
    }

    private void checkInvalidSumPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (isInvalidSumPrice(price, sum)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isInvalidSumPrice(BigDecimal price, BigDecimal sum) {
        return price.compareTo(sum) > 0;
    }

    private boolean isNotGrouppingMenu(Menu menu) {
        return !menuGroupDao.existsById(menu.getMenuGroupId());
    }

    private boolean isInvalidPrice(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
