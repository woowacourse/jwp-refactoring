package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.ui.request.ChangeNamePriceRequest;
import kitchenpos.menu.ui.request.CreateMenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest request) {
        menuValidator.validateMenuGroup(request.getMenuGroupId());
        final Menu menu = getMenu(request);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private Menu getMenu(CreateMenuRequest request) {
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        final List<MenuProduct> menuProducts = request.getMenuProducts()
                                                      .stream()
                                                      .map(item -> new MenuProduct(menu, item.getProductId(), item.getQuantity()))
                                                      .collect(Collectors.toList());
        menu.addMenuProducts(menuProducts, menuValidator);
        return menu;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuResponse::from)
                             .collect(Collectors.toList());
    }

    @Transactional
    public void changeNamePrice(Long menuId, ChangeNamePriceRequest request) {
        final Menu menu = menuRepository.findById(menuId)
                                  .orElseThrow(IllegalArgumentException::new);
        menu.changeNameAndPrice(request.getName(), request.getPrice());
        menuRepository.save(menu); // Domain Event 발행을 위해
    }
}
