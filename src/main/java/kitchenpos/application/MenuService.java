package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuRequest;
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
    public MenuResponse create(final MenuRequest request) {
        final Menu savedMenu = saveMenu(new Menu(
            request.getName(), new Price(request.getPrice()), request.getMenuGroupId()));
        final MenuProducts menuProducts = MenuProducts.from(request.getMenuProducts(), savedMenu);

        validateMenuPrice(savedMenu, menuProducts);

        List<MenuProduct> savedMenuProducts = saveMenuProducts(menuProducts);

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    private Menu saveMenu(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴는 반드시 하나의 메뉴그룹에 속해야합니다.");
        }
        return menuDao.save(menu);
    }

    private void validateMenuPrice(Menu menu, MenuProducts menuProducts) {
        if (menu.isPriceBiggerThen(sumProductPrices(menuProducts))) {
            throw new IllegalArgumentException(
                "메뉴의 가격은 해당 메뉴를 구성하는 상품들의 가격보다 작거나 같아야 합니다.");
        }
    }

    private Price sumProductPrices(MenuProducts menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return new Price(sum);
    }

    private List<MenuProduct> saveMenuProducts(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
            .map(menuProductDao::save)
            .collect(toList());
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
            .stream()
            .map(menu -> MenuResponse.of(menu, menuProductDao.findAllByMenuId(menu.getId())))
            .collect(toList());
    }
}
