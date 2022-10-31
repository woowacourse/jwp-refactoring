package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao, final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final Menu menu) {
        validatePrice(menu);
        validateMenuGroup(menu);
        comparePriceToAmount(menu);
        return menuDao.save(menu);
    }

    private static void validatePrice(final Menu menu) {
        if (menu.isNullOrNegativePrice()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void comparePriceToAmount(final Menu menu) {
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(menuProduct.calculateAmount(product.getPrice()));
        }
        if (menu.isGreaterPriceThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
