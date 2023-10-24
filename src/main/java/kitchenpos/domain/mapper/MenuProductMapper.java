package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.domain.MenuProduct;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private MenuProductMapper() {
    }

    public MenuProduct toMenuProduct(final MenuProductRequest request) {
        return MenuProduct.builder()
                .seq(request.getSeq())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
    }
}
