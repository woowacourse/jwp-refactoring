package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    public Menu create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();
        validateExistenceOfMenuGroup(request);
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final Menu savedMenu = saveMenu(request, price, menuProductRequests);
        saveMenuProducts(menuProductRequests, savedMenu);

        return savedMenu;
    }

    private void validateExistenceOfMenuGroup(final MenuRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal getSumOfMenuProductRequests(final List<MenuProductRequest> menuProductRequests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }
        return sum;
    }

    private void validatePriceAndSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private Menu saveMenu(final MenuRequest request, final BigDecimal price, final List<MenuProductRequest> requests) {
        final BigDecimal sum = getSumOfMenuProductRequests(requests);
        validatePriceAndSum(price, sum);
        return menuDao.save(
                new Menu(new MenuName(request.getName()), new MenuPrice(request.getPrice()), request.getMenuGroupId())
        );
    }

    private void saveMenuProducts(final List<MenuProductRequest> requests, final Menu savedMenu) {
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest request : requests) {
            final MenuProduct menuProduct = new MenuProduct(menuId, request.getProductId(), new Quantity(request.getQuantity()));
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.updateMenuProducts(savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }
}
