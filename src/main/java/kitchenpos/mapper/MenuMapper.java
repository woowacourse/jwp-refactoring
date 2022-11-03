package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.ProductNotFoundException;
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
