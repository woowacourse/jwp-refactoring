package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuValidator;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuDao menuDao,
            final MenuProductDao menuProductDao,
            final MenuValidator menuValidator) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final List<MenuProduct> menuProducts = createMenuProductsByMenuRequest(request);
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(),
                menuProducts);
        menuValidator.validate(menu);
        final Menu savedMenu = menuDao.save(menu);
        savedMenu.setMenuProducts(saveMenuProducts(savedMenu.getId(), menuProducts));
        return savedMenu;
    }

    private List<MenuProduct> createMenuProductsByMenuRequest(final MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<MenuProduct> saveMenuProducts(final Long menuId, final List<MenuProduct> menuProducts) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
