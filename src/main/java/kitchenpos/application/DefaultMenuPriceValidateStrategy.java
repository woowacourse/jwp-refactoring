package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.ProductNotFoundException;

@Component
public class DefaultMenuPriceValidateStrategy implements MenuPriceValidateStrategy {

    @Override
    public void validate(List<Product> products, List<MenuProductCreateRequest> menuProductRequests,
        BigDecimal menuPrice) {
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProduct : menuProductRequests) {
            Product product = productMap.get(menuProduct.getProductId());
            if (Objects.isNull(product)) {
                throw new ProductNotFoundException(menuProduct.getProductId());
            }
            sum = sum.add(
                product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException(menuPrice.longValue(), sum.longValue());
        }
    }
}
