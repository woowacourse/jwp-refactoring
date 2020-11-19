package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final Money price = product.getPrice();

        validPriceIsNullOrPriceIsMinus(price);

        return productRepository.save(product);
    }

    private void validPriceIsNullOrPriceIsMinus(Money price) {
        if (price == null || price.isMinus()) {
            throw new IllegalArgumentException();
        }
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
