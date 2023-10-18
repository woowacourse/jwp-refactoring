package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductInfo;
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

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuGroupRepository menuGroupRepository,
            MenuRepository menuRepository,
            MenuValidator menuValidator
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        List<MenuProduct> menuProducts = menuProducts(request.getMenuProducts());
        Menu menu = Menu.create(
                request.getName(),
                request.getPrice(),
                menuGroupRepository.getById(request.getMenuGroupId()),
                menuProducts,
                menuValidator
        );
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> menuProducts(List<MenuProductInfo> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .toList();
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .toList();
    }
}
