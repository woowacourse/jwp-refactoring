package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuCreateRequest.MenuProductInfo;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuValidator;
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
