package kitchenpos.validator;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;

public interface MenuPriceValidateStrategy {
    void validate(List<Product> products,
        List<MenuProductCreateRequest> menuProductRequests, BigDecimal menuPrice);
}
