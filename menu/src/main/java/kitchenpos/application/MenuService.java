package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.MenuValidator;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
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
