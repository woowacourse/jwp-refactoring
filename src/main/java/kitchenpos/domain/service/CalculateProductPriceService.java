package kitchenpos.domain.service;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CalculateProductPriceService {

    private final ProductRepository productRepository;

    public CalculateProductPriceService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BigDecimal calculateMenuProductPriceSum(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPriceValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }
}
