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
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(final MenuDao menuDao,
                       final MenuGroupDao menuGroupDao,
                       final MenuProductDao menuProductDao,
                       final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = menuRequest.toMenu();
        final List<MenuProduct> menuProducts = menuRequest.toMenuProducts();

        validateMenuGroup(menu);
        menu.validatePrice(calculateSumOfMenuPrice(menuProducts));

        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(menuProducts, savedMenu);

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> saveMenuProducts(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductDao.save(
                        new MenuProduct(savedMenu.getId(), menuProduct.getProductId(), menuProduct.getQuantity())))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateSumOfMenuPrice(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = getProduct(menuProduct);
            sum = sum.add(product.multiply(menuProduct.getQuantity()));
        }
        return sum;
    }

    private Product getProduct(final MenuProduct menuProduct) {
        return productDao.findById(menuProduct.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }
}
