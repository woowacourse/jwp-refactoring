package kitchenpos.domain;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PriceValidator {
    private Map<Long, Product> productMapper;

    private PriceValidator(Map<Long, Product> productMapper) {
        if (productMapper.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.productMapper = productMapper;
    }

    public static PriceValidator of(List<Product> products) {
        return products.stream()
            .collect(collectingAndThen(toMap(Product::getId, Function.identity()), PriceValidator::new));
    }

    public void validate(BigDecimal menuPrice, List<MenuProductCreateInfo> menuProductCreateInfos) {
        if (isMenuPriceExpensive(menuPrice, calculateTotal(menuProductCreateInfos))) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isMenuPriceExpensive(BigDecimal menuPrice, BigDecimal totalPrice) {
        return menuPrice.compareTo(totalPrice) > 0;
    }

    private BigDecimal calculateSubTotal(MenuProductCreateInfo menuProductCreateInfo) {
        Product product = productMapper.get(menuProductCreateInfo.getProductId());
        return product.calculatePrice(menuProductCreateInfo.getQuantity());
    }

    private BigDecimal calculateTotal(List<MenuProductCreateInfo> menuProductCreateInfos) {
        return menuProductCreateInfos.stream()
            .map(this::calculateSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
