package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menu.dto.request.MenuProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final ProductRepository productRepository;

    public MenuMapper(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Menu mappingToMenu(final MenuCreateRequest request) {
        List<MenuProduct> menuProducts = toMenuProducts(request.getMenuProducts());
        return request.toEntity(menuProducts);
    }

    private List<MenuProduct> toMenuProducts(final List<MenuProductCreateRequest> requests) {
        return requests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(final MenuProductCreateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return new MenuProduct(product, request.getQuantity());
    }
}
