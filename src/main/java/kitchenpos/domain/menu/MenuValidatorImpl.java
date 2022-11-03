package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidatorImpl implements MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidatorImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void validatePriceByProducts(BigDecimal price, List<MenuProduct> menuProducts, MenuRequest menuRequest) {
        final List<Product> products = mapToProducts(menuRequest);

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = getProductByMenuProduct(products, menuProduct);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private List<Product> mapToProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
                .stream()
                .map(this::findProductByMenuProduct)
                .collect(Collectors.toList());
    }

    private Product findProductByMenuProduct(MenuProduct menuProduct) {
        return productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product getProductByMenuProduct(List<Product> products, MenuProduct menuProduct) {
        return products.stream()
                .filter(product -> product.getId().equals(menuProduct.getProductId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("product를 찾을 수 없습니다."));
    }
}
