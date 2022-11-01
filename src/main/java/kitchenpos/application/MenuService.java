package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
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
    public Menu create(final MenuCreateRequest request) {
        checkMenuGroup(request);
        final List<MenuProductCreateRequest> menuProductsCreateRequest = request.getMenuProducts();

        Menu menu = new Menu(null, request.getName(), request.getPrice(), request.getMenuGroupId(), null);
        menu.checkMenuable(menuProductsCreateRequest);
        checkMenuPrice(menuProductsCreateRequest, menu);

        final Menu savedMenu = menuDao.save(menu);
        setMenuProducts(menuProductsCreateRequest, savedMenu);
        return savedMenu;
    }

    private void checkMenuGroup(MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void checkMenuPrice(List<MenuProductCreateRequest> menuProductsCreateRequest, Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProductRequest : menuProductsCreateRequest) {
            Product product = productDao.getById(menuProductRequest.getProductId());
            long quantity = menuProductRequest.getQuantity();
            sum = sum.add(product.getTotalPrice(quantity));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void setMenuProducts(List<MenuProductCreateRequest> menuProductsCreateRequest, Menu savedMenu) {
        Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProduct : menuProductsCreateRequest) {
            MenuProduct savedMenuProduct = menuProductDao.save(
                    new MenuProduct(null,
                            menuId,
                            menuProduct.getProductId(),
                            menuProduct.getQuantity()));
            savedMenuProducts.add(savedMenuProduct);
        }
        savedMenu.setMenuProducts(savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
