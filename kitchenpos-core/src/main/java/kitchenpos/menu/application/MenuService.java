package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.global.Price;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.model.MenuGroup;
import kitchenpos.menu.domain.model.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.domain.service.MenuValidator;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findByIdOrThrow(request.getMenuGroupId());
        Menu menu = menuRepository.save(
            Menu.create(request.getName(), new Price(request.getPrice()), menuGroup,
                createMenuProducts(request.getMenuProducts()), menuValidator));
        return MenuResponse.from(menu);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductCreateRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(menuProduct -> new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return menuRepository.findAllWithFetch().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
