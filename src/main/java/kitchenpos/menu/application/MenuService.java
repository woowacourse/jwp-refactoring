package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.ui.request.MenuChangeRequest;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductConnector menuProductConnector;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductConnector menuProductConnector
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductConnector = menuProductConnector;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = generateMenuProducts(menuRequest.getMenuProducts());
        menuProductConnector.validateMenuProducts(menuProducts, menuRequest.getPrice());

        final Menu menu = new Menu.Builder()
                .name(menuRequest.getName())
                .price(menuRequest.getPrice())
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();

        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }

    private List<MenuProduct> generateMenuProducts(List<MenuProductRequest> menuProductRequests) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            menuProductConnector.validateProductId(menuProductRequest.getProductId());

            final MenuProduct menuProduct = new MenuProduct.Builder()
                    .productId(menuProductRequest.getProductId())
                    .quantity(menuProductRequest.getQuantity())
                    .build();
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllFetchJoinMenuProducts();
        return MenuResponse.toList(menus);
    }

    @Transactional
    public MenuResponse changeMenu(MenuChangeRequest menuChangeRequest) {
        final Menu menu = menuRepository.findById(menuChangeRequest.getId())
                .orElseThrow(IllegalArgumentException::new);
        menu.changeName(menuChangeRequest.getName());
        menuProductConnector.validateMenuProducts(menu.getMenuProducts(), menuChangeRequest.getPrice());
        menu.changePrice(menuChangeRequest.getPrice());
        menuRepository.save(menu);
        return MenuResponse.of(menu);
    }
}
