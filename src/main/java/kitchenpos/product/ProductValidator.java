package kitchenpos.product;

import java.util.List;
import java.util.Optional;
import kitchenpos.product.domain.Price;

public interface ProductValidator {

    Optional<Price> calculateAmountSum(List<Long> productIds);

    boolean existsProductsByIdIn(List<Long> productIds);
}
