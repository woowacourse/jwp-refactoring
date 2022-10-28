package kitchenpos.application;

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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
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
        validateMenuPrice(menu.getPrice());
        validateMenuGroup(menu);
        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        validateMenuPrice(menu.getPrice(), calculateProductPrice(menuProducts));
        final Menu savedMenu = menuDao.save(menu);
        fillMenuIdToMenuProduct(menuProducts, savedMenu);
        return savedMenu;
    }

    private void validateMenuPrice(final BigDecimal menuPrice) {
        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 부적절한 메뉴 가격입니다.");
        }
    }

    private void validateMenuGroup(final Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("[ERROR] 메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void validateMenuPrice(final BigDecimal menuPrice, final BigDecimal sum) {
        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 메뉴 상들 가격의 합보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateProductPrice(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("[ERROR] 상품이 존재하지 않습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private void fillMenuIdToMenuProduct(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.addAllMenuProduct(savedMenuProducts);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.addAllMenuProduct(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }
}
