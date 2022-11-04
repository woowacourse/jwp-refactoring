package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
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
        validateMenuGroup(menuRequest);
        final Menu menu = toMenu(menuRequest);

        final Menu savedMenu = menuDao.save(menu);
        saveMenuProducts(menu.getMenuProducts(), savedMenu);

        return savedMenu;
    }

    private void validateMenuGroup(final MenuRequest menuRequest) {
        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private Menu toMenu(final MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                toMenuProducts(menuRequest.getMenuProducts()));
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(it -> {
                    final Product product = findProductById(it.getProductId());
                    return new MenuProduct(product.getId(), it.getQuantity(), product.getPrice());
                })
                .collect(Collectors.toList());
    }

    private Product findProductById(final Long productId) {
        return productDao.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void saveMenuProducts(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.addMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));

        }
        savedMenu.changeMenuProducts(savedMenuProducts);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
