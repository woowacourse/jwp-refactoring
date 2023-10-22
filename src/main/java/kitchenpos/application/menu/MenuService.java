package kitchenpos.application.menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreationRequest;
import kitchenpos.application.dto.MenuProductWithQuantityRequest;
import kitchenpos.application.dto.result.MenuResult;
import kitchenpos.dao.menu.MenuGroupRepository;
import kitchenpos.dao.menu.MenuProductRepository;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResult create(final MenuCreationRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("MenuGroup does not exist."));
        final Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup));
        final List<MenuProduct> menuProducts = getMenuProductsByRequest(menu, request.getMenuProducts());
        final Price totalPrice = calculateTotalPrice(menuProducts);
        validateMenuProductsPrice(menu, totalPrice);
        menuProductRepository.saveAll(menuProducts);
        return MenuResult.from(menu);
    }

    private List<MenuProduct> getMenuProductsByRequest(
            final Menu menu,
            final List<MenuProductWithQuantityRequest> menuProductRequests
    ) {
        final List<Long> productIds = extractProductIds(menuProductRequests);
        final Map<Long, Product> productsById = productRepository.findAllByIdIn(productIds)
                .stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        return menuProductRequests.stream().map(menuProductRequest -> {
            final Product product = getProductByRequestId(menuProductRequest, productsById);
            return new MenuProduct(menu, product.getId(), menuProductRequest.getQuantity());
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

    private Price calculateTotalPrice(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::calculateMenuProductPrice)
                .reduce(Price.getDefault(), Price::add);
    }

    private Price calculateMenuProductPrice(final MenuProduct menuProduct) {
        final Product product = productRepository.getReferenceById(menuProduct.getProductId());
        return product.getPrice().multiply(menuProduct.getQuantity());
    }

    public void validateMenuProductsPrice(final Menu menu, final Price totalPrice) {
        if (menu.getPrice().isGreaterThan(totalPrice)) {
            throw new IllegalArgumentException("Sum of menu products price must be greater than menu price.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResult> list() {
        return menuRepository.findAll().stream()
                .map(MenuResult::from)
                .collect(Collectors.toList());
    }
}
