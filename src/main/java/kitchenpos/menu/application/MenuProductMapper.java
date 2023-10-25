package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.vo.MenuProducts;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private final ProductRepository productRepository;

    public MenuProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MenuProducts toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = convertToMenuProducts(menuProductRequests);
        return new MenuProducts(menuProducts);
    }

    private List<MenuProduct> convertToMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = getProduct(menuProductRequest);
        return new MenuProduct(
                product.getId(),
                product.getName(),
                product.getPrice(),
                menuProductRequest.getQuantity()
        );
    }

    private Product getProduct(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));
    }
}
