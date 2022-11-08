package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.ProductRepository;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menus;
    private final ProductRepository products;
    private final MenuGroupRepository menuGroups;

    public MenuService(final MenuRepository menus,
                       final ProductRepository products,
                       final MenuGroupRepository menuGroups) {
        this.menus = menus;
        this.products = products;
        this.menuGroups = menuGroups;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final var menuGroup = menuGroups.get(request.getMenuGroupId());
        final var menuProducts = mapMenuProducts(request);

        return menus.add(
                new Menu(request.getName(),
                        request.getPrice(),
                        menuGroup.getId(),
                        menuProducts)
        );
    }

    private List<MenuProduct> mapMenuProducts(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> {
                    final var product = products.get(menuProductRequest.getProductId());
                    return new MenuProduct(null, product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menus.getAll();
    }

    @Transactional
    public Menu update(final Long menuId, final MenuUpdateRequest request) {
        final var oldMenu = menus.get(menuId);
        final var menuProducts = oldMenu.getMenuProducts();

        final var updatedMenu = new Menu(
                request.getName(),
                request.getPrice(),
                oldMenu.getMenuGroupId(),
                menuProducts
        );
        return menus.add(updatedMenu);
    }
}
