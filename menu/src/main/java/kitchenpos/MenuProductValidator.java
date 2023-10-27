package kitchenpos;

import kitchenpos.application.request.MenuRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuProductValidator {
    private final ProductRepository productRepository;

    public MenuProductValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(final MenuRequest request, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = getSumOfMenuProductRequests(menuProducts);
        validatePriceAndSum(request.getPrice(), sum);
    }

    private BigDecimal getSumOfMenuProductRequests(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantityValue())));
        }
        return sum;
    }

    private void validatePriceAndSum(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
