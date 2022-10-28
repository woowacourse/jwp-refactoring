package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
    public Menu create(final Menu request) {
        final var menuGroup = menuGroups.get(request.getMenuGroupId());
        final var menuProducts = mapMenuProducts(request);

        final var menu = new Menu(request.getName(), request.getPrice(), menuGroup.getId(), menuProducts);
        return menus.add(menu);
    }

    private List<MenuProduct> mapMenuProducts(final Menu request) {
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
}
