package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public class Products {

    private final List<Product> values;

    public Products(final List<Product> values) {
        this.values = values;
    }

    public void validateSum(final List<Integer> counts, final BigDecimal price) {
        final BigDecimal sum = calculateSum(counts);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("주문 금액이 총 상품 금액보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateSum(final List<Integer> counts) {
        return IntStream.range(0, values.size())
                .mapToObj(index -> values.get(index).getPrice().multiply(BigDecimal.valueOf(counts.get(index))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
