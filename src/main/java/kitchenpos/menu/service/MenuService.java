package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        List<MenuProduct> menuProducts = toMenuProducts(menuCreateRequest.getMenuProducts());
        Menu menu = Menu.of(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId(), menuProducts, menuValidator);
        return new MenuResponse(menuRepository.save(menu));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductCreateRequest> menuProductCreateRequests) {
        return menuProductCreateRequests.stream()
                .map(menuProductCreateRequest -> new MenuProduct(
                                menuProductCreateRequest.getProductId(),
                                new Quantity(menuProductCreateRequest.getQuantity()
                                )
                        )
                )
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
