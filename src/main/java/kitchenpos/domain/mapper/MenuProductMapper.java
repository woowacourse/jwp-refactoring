package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private final ProductRepository productRepository;

    private MenuProductMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MenuProduct toMenuProduct(final MenuProductRequest request) {
        return MenuProduct.builder()
                .seq(request.getSeq())
                .product(getProductById(request.getProductId()))
                .quantity(request.getQuantity())
                .build();
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

}
