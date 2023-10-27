package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroupValidator;
import kitchenpos.menu.MenuName;
import kitchenpos.menu.MenuPrice;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProductValidator;
import kitchenpos.menu.MenuQuantity;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupValidator menuGroupValidator;
    private final MenuProductValidator menuProductValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupValidator menuGroupValidator,
                       final MenuProductValidator menuProductValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupValidator = menuGroupValidator;
        this.menuProductValidator = menuProductValidator;
    }

    public Menu create(final MenuRequest request) {
        menuGroupValidator.validate(request.getMenuGroupId());
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(m -> new MenuProduct(m.getProductId(), new MenuQuantity(m.getQuantity())))
                .collect(Collectors.toList());

        menuProductValidator.validate(request, menuProducts);

        return menuRepository.save(
                new Menu(
                        new MenuName(request.getName()),
                        new MenuPrice(request.getPrice()),
                        request.getMenuGroupId(),
                        menuProducts
                )
        );
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
