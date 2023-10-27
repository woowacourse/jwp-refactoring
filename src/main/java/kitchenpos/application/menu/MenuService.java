package kitchenpos.application.menu;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuProductPriceSum(menuRequest);
        
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu newMenu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu savedMenu = menuDao.save(newMenu);

        final List<MenuProduct> savedMenuProducts = createMenuProducts(savedMenu, menuRequest.getMenuProductRequests());
        savedMenu = new Menu(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                savedMenu.getMenuGroup(), savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    private void validateMenuProductPriceSum(MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (menuRequest.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> createMenuProducts(Menu savedMenu, List<MenuProductRequest> menuProductRequests) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            MenuProduct menuProduct = new MenuProduct(savedMenu.getId(),
                    menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> new Menu(menu.getId(), menu.getName(), menu.getPrice(),
                        menu.getMenuGroup(), menuProductDao.findAllByMenuId(menu.getId())))
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
