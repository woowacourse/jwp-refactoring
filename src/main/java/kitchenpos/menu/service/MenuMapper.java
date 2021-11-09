package kitchenpos.menu.service;

import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    private final ProductRepository productRepository;

    public MenuMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Menu mapFrom(MenuRequest menuRequest) {
        return new Menu(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                menuRequest.getMenuProductRequests()
                        .stream()
                        .map(this::toMenuProduct)
                        .collect(Collectors.toList())
        );
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(
                productRepository.findById(menuProductRequest.getProductId())
                        .orElseThrow(IllegalArgumentException::new),
                menuProductRequest.getQuantity());
    }
}
