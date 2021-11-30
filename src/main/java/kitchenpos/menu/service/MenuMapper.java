package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
                menuProductsFrom(menuRequest)
        );
    }

    public List<MenuProduct> menuProductsFrom(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts().stream().map(this::toMenuProduct).collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }
}
