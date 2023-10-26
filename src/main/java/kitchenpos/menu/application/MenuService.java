package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenusResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupException.NotFoundMenuGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
            final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(NotFoundMenuGroupException::new);

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(), menuProductRequest.getQuantity())
                ).collect(Collectors.toUnmodifiableList());

        menuValidator.validateCreate(request.getPrice(), menuProducts);
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    public MenusResponse list() {
        return MenusResponse.from(menuRepository.findAll());
    }
}
