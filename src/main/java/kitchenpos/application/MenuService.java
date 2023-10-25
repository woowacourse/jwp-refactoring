package kitchenpos.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.request.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        Menu menu = Menu.of(
                request.getName(),
                request.getPrice(),
                findMenuGroup(request.getMenuGroupId())
        );

        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());

        menu.addAllMenuProducts(menuProducts);

        return menuRepository.save(menu);
    }

    private MenuProduct createMenuProduct(MenuProductCreateRequest menuProductCreateRequest) {
        return MenuProduct.of(
                findProduct(menuProductCreateRequest.getProductId()),
                menuProductCreateRequest.getQuantity()
        );
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        validateNull(menuGroupId);

        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProduct(Long productId) {
        validateNull(productId);

        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

}
