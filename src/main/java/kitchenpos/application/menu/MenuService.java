package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreationRequest;
import kitchenpos.application.dto.result.MenuResult;
import kitchenpos.dao.menu.MenuGroupRepository;
import kitchenpos.dao.menu.MenuProductRepository;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository,
            final MenuValidator menuValidator
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResult create(final MenuCreationRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("MenuGroup does not exist."));
        final Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));
        final List<MenuProduct> menuProducts = extractMenuProductFromRequest(request);
        menu.addMenuProducts(menuProducts, menuValidator);
        menuProductRepository.saveAll(menuProducts);
        return MenuResult.from(menu);
    }

    private List<MenuProduct> extractMenuProductFromRequest(final MenuCreationRequest request) {
        return request.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResult> list() {
        return menuRepository.findAll().stream()
                .map(MenuResult::from)
                .collect(Collectors.toList());
    }
}
