package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.CreateMenuRequest;
import kitchenpos.menu.dto.ListMenuResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroups.domain.MenuGroup;
import kitchenpos.menugroups.domain.MenuGroupRepository;
import kitchenpos.menugroups.exception.MenuGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

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
