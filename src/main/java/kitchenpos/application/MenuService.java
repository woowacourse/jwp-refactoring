package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.menu.MenuCreateRequest;
import kitchenpos.util.BigDecimalUtil;
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
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        final Menu menu = createMenuByRequest(menuCreateRequest);
        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveAllMenuProducts(menu.getMenuProducts(), savedMenu.getId());
        savedMenu.addMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private Menu createMenuByRequest(final MenuCreateRequest request) {
        final String name = request.getName();
        final BigDecimal price = request.getPrice();
        final Long menuGroupId = request.getMenuGroupId();
        final List<MenuProduct> menuProducts = request.getMenuProducts();

        validateMenuGroupExist(request.getMenuGroupId());
        validateMenuPriceNotBiggerThanMenuProductsTotalPrice(price, menuProducts);

        return Menu.builder(name, price, menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }

    private void validateMenuGroupExist(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuPriceNotBiggerThanMenuProductsTotalPrice(final BigDecimal price,
                                                                      final List<MenuProduct> menuProducts) {
        final BigDecimal menuProductsTotalPrice = getMenuProductsTotalPrice(menuProducts);
        BigDecimalUtil.valueForCompare(price)
                .throwIfBiggerThan(menuProductsTotalPrice, IllegalArgumentException::new);
    }

    private BigDecimal getMenuProductsTotalPrice(final List<MenuProduct> menuProducts) {
        final List<BigDecimal> menuProductTotalPrices = menuProducts.stream()
                .map(this::getProductTotalPrice)
                .collect(Collectors.toList());

        return BigDecimalUtil.sum(menuProductTotalPrices);
    }

    private BigDecimal getProductTotalPrice(final MenuProduct menuProduct) {
        final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return product.getTotalPrice(BigDecimal.valueOf(menuProduct.getQuantity()));
    }

    private List<MenuProduct> saveAllMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
