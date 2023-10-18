package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductInfo;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuDao menuDao,
            MenuProductDao menuProductDao,
            MenuValidator menuValidator
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = menuProducts(request.getMenuProducts());
        Menu menu = Menu.create(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                menuProducts,
                menuValidator
        );
        final Menu savedMenu = menuDao.save(menu);
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private List<MenuProduct> menuProducts(List<MenuProductInfo> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .toList();
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
