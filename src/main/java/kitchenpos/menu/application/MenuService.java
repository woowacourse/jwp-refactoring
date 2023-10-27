package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.ui.request.MenuCreateRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Long create(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(it -> new MenuProduct(
                                it.getProductId(),
                                it.getQuantity()
                        )
                ).collect(Collectors.toList());

        final Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                menuProducts
        );
        menu.validate(menuValidator);

        return menuRepository.save(menu).getId();
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
