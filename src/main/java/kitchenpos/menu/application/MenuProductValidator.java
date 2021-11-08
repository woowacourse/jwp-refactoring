package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class MenuProductValidator {

    private final ProductRepository productRepository;

    public MenuProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateMenuProduct(List<MenuProduct> menuProducts, BigDecimal price) {
        if (Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException();
        }
        BigDecimal totalPriceOfSingleMenuProduct = calculateMenuProductsPrice(menuProducts);
        if (price.compareTo(totalPriceOfSingleMenuProduct) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateMenuProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal totalPriceOfSingleMenuProduct = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            final BigDecimal menuProductPrice = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            totalPriceOfSingleMenuProduct = totalPriceOfSingleMenuProduct.add(menuProductPrice);
        }
        return totalPriceOfSingleMenuProduct;
    }
}
