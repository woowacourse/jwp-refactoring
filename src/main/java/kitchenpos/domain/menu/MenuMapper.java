package kitchenpos.domain.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final ProductRepository productRepository;

    public MenuMapper(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Menu toMenu(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
        return new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
    }

    private MenuProduct toMenuProduct(final MenuProductRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product.getId(), request.getQuantity());
    }
}
