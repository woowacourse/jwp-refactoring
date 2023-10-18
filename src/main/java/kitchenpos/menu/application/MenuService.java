package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductInfo;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = menuProducts(request.getMenuProducts());
        Menu menu = Menu.create(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                menuProducts,
                menuValidator
        );
        return menuRepository.save(menu);
    }

    private List<MenuProduct> menuProducts(List<MenuProductInfo> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .toList();
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
