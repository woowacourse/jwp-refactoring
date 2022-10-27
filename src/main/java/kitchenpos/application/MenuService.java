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
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
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
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductCreateRequest> menuProductsRequest = request.getMenuProducts();

        Menu menu = new Menu(null, request.getName(), request.getPrice(), request.getMenuGroupId(), null);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProductRequest : menuProductsRequest) {
            Product product = productDao.getById(menuProductRequest.getProductId());
            BigDecimal price = product.getPrice();
            long quantity = menuProductRequest.getQuantity();
            sum = sum.add(price).multiply(BigDecimal.valueOf(quantity));
        }

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductRequest : menuProductsRequest) {
            MenuProduct savedMenuProduct = menuProductDao.save(
                    new MenuProduct(null,
                            savedMenu.getId(),
                            menuProductRequest.getProductId(),
                            menuProductRequest.getQuantity()));
            savedMenuProducts.add(savedMenuProduct);
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
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
