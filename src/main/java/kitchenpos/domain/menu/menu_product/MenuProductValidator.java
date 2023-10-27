package kitchenpos.domain.menu.menu_product;

import kitchenpos.domain.product.Product;
import kitchenpos.support.AggregateReference;
import kitchenpos.exception.MenuException;
import kitchenpos.repositroy.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuProductValidator {

    private final ProductRepository productRepository;

    public MenuProductValidator(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(final AggregateReference<Product> productId) {
        if (!productRepository.existsById(productId.getId())) {
            throw new MenuException.NoMenuProductException();
        }
    }
}
