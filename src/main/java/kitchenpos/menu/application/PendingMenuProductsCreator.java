package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.PendingMenuProduct;
import kitchenpos.menu.domain.PendingMenuProducts;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class PendingMenuProductsCreator {

    private final ProductRepository productRepository;

    public PendingMenuProductsCreator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PendingMenuProducts create(final MenuRequest request) {
        final List<Product> products = findAllProductsByIdIn(request);
        final Map<Long, Long> quantities = request.getMenuProducts().stream()
                .collect(Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));
        final List<PendingMenuProduct> pendingMenuProducts = products.stream()
                .map(p -> new PendingMenuProduct(p.getId(), p.getPrice(), quantities.get(p.getId())))
                .collect(Collectors.toList());
        return new PendingMenuProducts(pendingMenuProducts);
    }

    private List<Product> findAllProductsByIdIn(final MenuRequest request) {
        final List<Long> productIds = request.getMenuProducts().stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        final List<Product> products = productRepository.findAllByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException();
        }
        return products;
    }
}
