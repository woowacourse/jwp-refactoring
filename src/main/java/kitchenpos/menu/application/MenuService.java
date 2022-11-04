package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuCreateResponse;
import kitchenpos.menu.dto.MenuFindResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
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
    public MenuCreateResponse create(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        validateMenuPrice(menuCreateRequest.getPrice(), menuCreateRequest.getMenuProducts());

        final Menu savedMenu = menuDao.save(
                new Menu(
                        menuCreateRequest.getName(),
                        menuCreateRequest.getPrice(),
                        menuCreateRequest.getMenuGroupId()
                )
        );
        updateMenuProductsByMenuId(menuCreateRequest.getMenuProducts(), savedMenu);

        return new MenuCreateResponse(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getMenuGroupId(),
                savedMenu.getAllMenuProduct()
        );
    }

    private void validateMenuPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void updateMenuProductsByMenuId(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        savedMenu.addMenuIdToMenuProducts();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProductDao.update(menuProduct);
        }
        savedMenu.changeAllMenuProducts(new MenuProducts(menuProducts));
    }

    public List<MenuFindResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(MenuFindResponse::from)
                .collect(Collectors.toList());
    }
}
