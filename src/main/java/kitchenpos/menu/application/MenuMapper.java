package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.request.MenuRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuGroupRepository menuGroupRepository;

    private final MenuValidator menuValidator;

    public MenuMapper(final MenuGroupRepository menuGroupRepository, final MenuValidator menuValidator) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    public Menu from(final MenuRequest menuRequest) {
        return new Menu(
                menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                getMenuGroup(menuRequest),
                getMenuProducts(menuRequest),
                menuValidator
        );
    }

    private MenuGroup getMenuGroup(final MenuRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()
                ))
                .collect(Collectors.toList());
        // TODO: 최적화 필요
    }
}
