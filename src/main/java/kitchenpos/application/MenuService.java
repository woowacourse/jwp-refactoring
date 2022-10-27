package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
        validateInvalidPrice(menu.getPrice());
        validateExistsMenuGroupId(menu);

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal totalSum = BigDecimal.ZERO;
        final List<Long> productIds = toProductIds(menuProducts);
        final Map<Long, Product> products = findProducts(productIds);

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = products.get(menuProduct.getProductId());
            final BigDecimal multiplePrice = product.multiplePrice(menuProduct.getQuantity());
            totalSum = totalSum.add(multiplePrice);
        }

        if (menu.getPrice().compareTo(totalSum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = updateMenuProducts(menuProducts, savedMenu.getId());
        savedMenu.addMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    private void validateInvalidPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistsMenuGroupId(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> toProductIds(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> updateMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    private Map<Long, Product> findProducts(final List<Long> productIds) {
        return productDao.findByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, it -> it));
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
