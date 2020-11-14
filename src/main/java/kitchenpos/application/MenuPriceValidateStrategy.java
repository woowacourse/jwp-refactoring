package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;

public interface MenuPriceValidateStrategy {
    void validate(List<Product> products,
        List<MenuProductCreateRequest> menuProductRequests, BigDecimal menuPrice);
}
