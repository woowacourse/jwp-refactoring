package kitchenpos.domain.menu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Price;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validatePrice(final List<MenuProduct> menuProducts, final Price price) {
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());

        Map<Long, Long> productQuantities = menuProducts.stream()
                .collect(Collectors.toMap(
                        MenuProduct::getProductId,
                        MenuProduct::getQuantity
                ));

        Price sum = productRepository.findByIdIn(productIds)
                .stream()
                .map(product -> product.getPrice().multiply(productQuantities.get(product.getId())))
                .reduce(Price::add)
                .orElse(Price.ZERO);

        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException();
        }
    }
}
