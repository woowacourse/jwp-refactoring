package kitchenpos.application;

import kitchenpos.Menu;
import kitchenpos.MenuGroupValidator;
import kitchenpos.MenuName;
import kitchenpos.MenuPrice;
import kitchenpos.MenuProduct;
import kitchenpos.MenuProductValidator;
import kitchenpos.MenuQuantity;
import kitchenpos.MenuRepository;
import kitchenpos.application.request.MenuProductRequest;
import kitchenpos.application.request.MenuRequest;
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
