package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.validator.MenuValidator;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.application.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = createMenuProducts(menuRequest);
        final Menu menu = Menu.create(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                menuProducts,
                menuValidator);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(final MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final MenuProduct menuProduct = new MenuProduct(
                    null,
                    menuProductRequest.getProductId(),
                    menuProductRequest.getQuantity());
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
