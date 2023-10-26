package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.MenuProductRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private final ProductRepository productRepository;

    private MenuProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MenuProduct toMenuProduct(final MenuProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return MenuProduct.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
