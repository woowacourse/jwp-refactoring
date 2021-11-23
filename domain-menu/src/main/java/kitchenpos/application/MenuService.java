package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuValidator;
import kitchenpos.dto.request.ChangeNamePriceRequest;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;

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
        return MenuResponse.from(menuRepository.save(menu));
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
