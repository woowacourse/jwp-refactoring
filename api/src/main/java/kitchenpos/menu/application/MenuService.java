package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.ProductQuantityDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.vo.Price;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu menu = convertToMenu(menuRequest);
        menuValidator.validate(menu);

        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private Menu convertToMenu(final MenuRequest menuRequest) {
        return Menu.of(menuRequest.getName(),
            Price.from(menuRequest.getPrice()),
            menuRequest.getMenuGroupId(),
            getMenuProductsFromRequest(menuRequest.getMenuProducts())
        );
    }

    private List<MenuProduct> getMenuProductsFromRequest(final List<ProductQuantityDto> requests) {
        return requests.stream()
            .map(request -> new MenuProduct(request.getProductId(), request.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
