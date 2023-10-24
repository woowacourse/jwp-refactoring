package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(
            MenuValidator menuValidator,
            MenuGroupRepository menuGroupRepository,
            MenuRepository menuRepository
    ) {
        this.menuValidator = menuValidator;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId()),
                menuProducts,
                menuValidator
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
