package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    public Long create(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = createMenuProducts(request);
        menuValidator.validate(request.getPrice(), menuProducts);
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup, menuProducts);
        final Menu saveMenu = menuRepository.save(menu);
        return saveMenu.getId();
    }

    private List<MenuProduct> createMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts().stream()
                .map(menuProductRequest -> MenuProduct.of(
                        menuProductRequest.getProductId(), menuProductRequest.getQuantity())
                ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
