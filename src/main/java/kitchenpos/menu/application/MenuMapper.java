package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreationRequest;
import kitchenpos.menu.application.dto.MenuProductWithQuantityRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuMapper(
            final MenuGroupRepository menuGroupRepository,
            final MenuValidator menuValidator
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    public Menu from(final MenuCreationRequest request) {
        final MenuGroup menuGroup = getMenuGroup(request.getMenuGroupId());
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final List<MenuProduct> menuProducts = menuProductFromRequest(request.getMenuProducts());
        menu.addMenuProducts(menuProducts, menuValidator);
        return menu;
    }

    private MenuGroup getMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("MenuGroup does not exist."));
    }

    private List<MenuProduct> menuProductFromRequest(final List<MenuProductWithQuantityRequest> request) {
        return request.stream().map(menuProduct ->
                new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity())
        ).collect(Collectors.toList());
    }
}
