package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreationRequest;
import kitchenpos.ui.dto.MenuProductWithQuantityRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuRepository menuRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final MenuCreationRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("MenuGroup does not exist."));
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        List<MenuProduct> menuProducts = getMenuProductsByRequest(menu, request.getMenuProducts());
        menu.applyMenuProducts(menuProducts);
        return menuRepository.save(menu);
    }

    private List<MenuProduct> getMenuProductsByRequest(
            final Menu menu,
            final List<MenuProductWithQuantityRequest> menuProductRequests
    ) {
        return menuProductRequests.stream().map(menuProductRequest -> {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
            return new MenuProduct(menu, product, menuProductRequest.getQuantity());
        }).collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
