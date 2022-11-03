package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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
    public MenuResponse create(final MenuRequest request) {
        validateInvalidPrice(request.getPrice());
        validateExistsMenuGroupId(request.getMenuGroupId());

        Menu menu = request.toEntity();
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        Map<Long, Product> products = findProducts(menuProducts);
        BigDecimal totalSum = menu.calculateTotalSum(products);
        validateMenuPriceBiggerThanSum(menu, totalSum);

        final Menu savedMenu = menuDao.save(menu);
        savedMenu.changeMenuIdInMenuProducts();

        return MenuResponse.of(savedMenu);
    }

    private void validateInvalidPrice(final Long price) {
        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistsMenuGroupId(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuPriceBiggerThanSum(final Menu menu, final BigDecimal totalSum) {
        if (menu.isBiggerPrice(totalSum)) {
            throw new IllegalArgumentException();
        }
    }

    private Map<Long, Product> findProducts(final List<MenuProduct> menuProducts) {
        final List<Long> productIds = toProductIds(menuProducts);
        return productDao.findByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, it -> it));
    }

    private List<Long> toProductIds(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
