package kitchenpos.domain.menu;

import kitchenpos.dto.request.MenuProductRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuProductMapper {

    private MenuProductMapper() {
    }

    public MenuProduct toMenuProduct(final MenuProductRequest request) {
        return MenuProduct.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
    }
}
