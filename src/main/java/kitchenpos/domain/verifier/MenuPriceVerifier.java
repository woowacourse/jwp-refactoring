package kitchenpos.domain.verifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.ProductRepository;

@Component
public class MenuPriceVerifier implements MenuVerifier {

    private final ProductRepository productRepository;

    public MenuPriceVerifier(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void verify(List<Long> productIds, List<MenuProduct> menuProducts, BigDecimal menuPrice) {
        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

        if (Objects.isNull(menuPrice) || menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            Product product = productMap.get(menuProduct.getProductId());
            if (Objects.isNull(product)) {
                throw new ProductNotFoundException(menuProduct.getProductId());
            }
            sum = sum.add(product.calculatePrice(menuProduct.getQuantity()));

        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException(menuPrice.longValue(), sum.longValue());
        }
    }
}
