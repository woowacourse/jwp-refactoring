package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.ProductRepository;
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
        validateExistedMenuGroup(request);
        final List<Long> productIds = extractIds(request.getMenuProducts());
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final List<Product> products = productRepository.findByIdIn(productIds);
        validateAllProductsFound(productIds, products);
        final Map<Product, Integer> productWithQuantity = makeProductWithQuantity(request.getMenuProducts(), products);
        final Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup, productWithQuantity);
        return menuRepository.save(menu).getId();
    }

    private static List<Long> extractIds(final List<MenuProductRequest> menuCreateRequest) {
        return menuCreateRequest.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> findAll() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private Map<Product, Integer> makeProductWithQuantity(
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

    private static void validateAllProductsFound(final List<Long> productIdToQuantity, final List<Product> products) {
        if (products.stream().noneMatch(product -> productIdToQuantity.contains(product.getId()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateExistedMenuGroup(final MenuCreateRequest menuCreateRequest) {
        if (!menuGroupRepository.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}
