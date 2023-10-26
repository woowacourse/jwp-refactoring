package kitchenpos.application;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public Long create(final MenuCreateRequest request) {
        final List<Product> products = findAllProducts(request.extractIds());
        final Map<Product, Integer> productWithQuantity = makeProductWithQuantityMap(request.getMenuProducts(), products);
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup, productWithQuantity);
        final Menu saveMenu = menuRepository.save(menu);
        return saveMenu.getId();
    }

    private List<Product> findAllProducts(List<Long> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        validateAllProductsFound(productIds, products);
        return products;
    }

    private Map<Product, Integer> makeProductWithQuantityMap(
            final List<MenuProductRequest> menuProductRequests,
            final List<Product> products
    ) {
        final Map<Long, Integer> productIdToQuantity = menuProductRequests.stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));

        return products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> productIdToQuantity.get(product.getId())
                ));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private void validateAllProductsFound(final List<Long> productIds, final List<Product> products) {
        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException();
        }
    }
}
