package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import kitchenpos.domain.Price;

public class Products {

    private final List<Product> values;

    public Products(List<Product> values) {
        validate(values);
        this.values = values;
    }

    private void validate(List<Product> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }
    }

    public Price calculateAmount(Map<Long, Long> productIdsQuantity) {
        return new Price(values.stream()
                .map(product -> multiply(productIdsQuantity, product))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal multiply(Map<Long, Long> productIdsQuantity, Product product) {
        return product.getPrice().getValue()
                .multiply(quantity(productIdsQuantity, product));
    }

    private BigDecimal quantity(Map<Long, Long> productIdsQuantity, Product product) {
        return BigDecimal.valueOf(productIdsQuantity.get(product.getId()));
    }
}
