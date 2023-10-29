package domain.menu_product;

import domain.Product;
import exception.MenuException;
import org.springframework.stereotype.Component;
import repository.ProductRepository;
import support.AggregateReference;

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
