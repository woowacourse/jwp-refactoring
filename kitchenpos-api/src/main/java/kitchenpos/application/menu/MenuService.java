package kitchenpos.application.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.application.menu.dto.MenuResponse;
import kitchenpos.application.menu.dto.MenuValidator;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroupValidator;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupValidator menuGroupValidator;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuProductRepository menuProductRepository,
                       final MenuGroupValidator menuGroupValidator,
                       final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        final Menu menu = createMenuByRequest(menuCreateRequest);
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = saveAllMenuProducts(menu.getMenuProducts(), savedMenu.getId());
        savedMenu.addMenuProducts(savedMenuProducts);
        return MenuResponse.from(savedMenu);
    }

    private Menu createMenuByRequest(final MenuCreateRequest request) {
        final String name = request.getName();
        final BigDecimal price = request.getPrice();
        final Long menuGroupId = request.getMenuGroupId();
        final List<MenuProduct> menuProducts = MenuProductMapper.mapToList(request.getMenuProducts());

        menuGroupValidator.validateMenuGroupExist(request.getMenuGroupId());
        menuValidator.validateMenuPriceNotBiggerThanMenuProductsTotalPrice(price, menuProducts);

        return Menu.builder(name, price, menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }

    private List<MenuProduct> saveAllMenuProducts(final List<MenuProduct> menuProducts, final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.belongToMenu(menuId);
            final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenuProducts.add(savedMenuProduct);
        }
        return savedMenuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            menu.addMenuProducts(menuProducts);
        }

        return MenuResponse.listOf(menus);
    }
}
