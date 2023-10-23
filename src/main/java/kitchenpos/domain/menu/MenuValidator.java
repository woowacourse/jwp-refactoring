package kitchenpos.domain.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.product.ProductRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProducts(final List<MenuProduct> menuProducts, final Price menuPrice) {
        final List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
        final List<Product> products = productRepository.findAllByIdIn(productIds);
        validateAllProductExist(menuProducts, products);
        validateProductPrice(menuProducts, menuPrice);
    }

    private void validateAllProductExist(final List<MenuProduct> menuProducts, final List<Product> products) {
        if (products.size() == menuProducts.size()) {
            return;
        }
        throw new IllegalArgumentException("Product does not exist.");
    }

    private void validateProductPrice(final List<MenuProduct> menuProducts, final Price menuPrice) {
        final Price productsPrice = menuProducts.stream()
                .map(this::calculateMenuProductPrice)
                .reduce(Price.getDefault(), Price::add);

        if (menuPrice.isGreaterThan(productsPrice)) {
            throw new IllegalArgumentException("Sum of menu products price must be greater than menu price.");
        }
    }

    private Price calculateMenuProductPrice(final MenuProduct menuProduct) {
        final Product product = productRepository.getReferenceById(menuProduct.getProductId());
        return product.getPrice().multiply(menuProduct.getQuantity());
    }
}
