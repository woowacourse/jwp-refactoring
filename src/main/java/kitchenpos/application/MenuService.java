package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuProductCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
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
    public MenuResponse create(final MenuCreateRequest request) {
        validateMenuGroup(request.getMenuGroupId());
        BigDecimal menuProductTotalPrice = calculateMenuProductTotalPrice(request.getMenuProducts());
        Menu menu = request.toMenu(menuProductTotalPrice);

        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menu.getMenuProducts(), savedMenu.getId());

        savedMenu.setMenuProducts(savedMenuProducts);
        return MenuResponse.from(savedMenu);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateMenuProductTotalPrice(List<MenuProductCreateRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productDao.findById(menuProduct.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return calculateMenuProductPrice(menuProduct, product);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMenuProductPrice(MenuProductCreateRequest menuProduct, Product product) {
        BigDecimal productPrice = product.getPriceValue();
        long quantity = menuProduct.getQuantity();
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProduct> menuProducts, Long menuId) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    menuProduct.setMenuId(menuId);
                    return menuProductDao.save(menuProduct);
                })
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
