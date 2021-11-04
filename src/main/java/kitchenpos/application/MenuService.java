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
import kitchenpos.dto.MenuRequest;
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
    public Menu create(final MenuRequest menuRequest) {
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuRequest.getMenuProducts());
        checkValidMenu(menu);

        return saveMenu(menu);
    }

    private Menu saveMenu(Menu menu) {
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        menu.setMenuIdInProducts(savedMenu.getId());
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return menuDao.findById(savedMenu.getId()).orElseThrow(IllegalArgumentException::new);
    }

    private void checkValidMenu(Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        final BigDecimal price = menu.getPrice();
        if (isInvalidPrice(price)) {
            throw new IllegalArgumentException();
        }

        if (isNotGroupingMenu(menu)) {
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

    private boolean isNotGroupingMenu(Menu menu) {
        return !menuGroupDao.existsById(menu.getMenuGroupId());
    }

    private boolean isInvalidPrice(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
