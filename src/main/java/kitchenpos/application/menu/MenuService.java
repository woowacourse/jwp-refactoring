package kitchenpos.application.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.ListMenuResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.menugroup.MenuGroupNotFoundException;
import kitchenpos.exception.product.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = findMenuGroup(request);
        final List<Product> products = findProducts(request);
        final List<Long> quantities = collectQuantities(request);
        final MenuProducts menuProducts = MenuProducts.from(products, quantities);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup.getId(), menuProducts);
        menuProducts.setMenu(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroup(final CreateMenuRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupNotFoundException(request.getMenuGroupId()));
    }

    private List<Long> collectQuantities(final CreateMenuRequest request) {
        return request.getMenuProducts().stream()
                .map(MenuProductRequest::getQuantity)
                .collect(Collectors.toList());
    }

    private List<Product> findProducts(final CreateMenuRequest request) {
        return request.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .map(this::findProduct)
                .collect(Collectors.toList());
    }

    private Product findProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public ListMenuResponse list() {
        return ListMenuResponse.from(menuRepository.findAll());
    }
}
