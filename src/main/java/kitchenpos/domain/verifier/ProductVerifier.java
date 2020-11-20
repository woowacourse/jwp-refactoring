package kitchenpos.domain.verifier;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.domain.MenuProduct;

public interface ProductVerifier {
    void verifyPrice(List<Long> productIds, List<MenuProduct> menuProducts, BigDecimal menuPrice);
}
