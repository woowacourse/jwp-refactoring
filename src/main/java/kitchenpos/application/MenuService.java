package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreationRequest;
import kitchenpos.application.dto.MenuProductWithQuantityRequest;
import kitchenpos.application.dto.result.MenuResult;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public MenuResult create(final MenuCreationRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("MenuGroup does not exist."));
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final List<MenuProduct> menuProducts = getMenuProductsByRequest(menu, request.getMenuProducts());
        menu.applyMenuProducts(menuProducts);
        return MenuResult.from(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProductsByRequest(
            final Menu menu,
            final List<MenuProductWithQuantityRequest> menuProductRequests
    ) {
        final List<Long> productIds = extractProductIds(menuProductRequests);
        final Map<Long, Product> productsById = productRepository.findAlLByIdIn(productIds)
                .stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        return menuProductRequests.stream().map(menuProductRequest -> {
            final Product product = getProductByRequestId(menuProductRequest, productsById);
            return new MenuProduct(menu, product, menuProductRequest.getQuantity());
        }).collect(Collectors.toList());
    }

    private List<Long> extractProductIds(final List<MenuProductWithQuantityRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(MenuProductWithQuantityRequest::getProductId)
                .collect(Collectors.toList());
    }

    private Product getProductByRequestId(
            final MenuProductWithQuantityRequest productId,
            final Map<Long, Product> productsById
    ) {
        return productsById.computeIfAbsent(productId.getProductId(), id -> {
            throw new IllegalArgumentException("Product does not exist.");
        });
    }

    public List<MenuResult> list() {
        return menuRepository.findAll().stream()
                .map(MenuResult::from)
                .collect(Collectors.toList());
    }
}
