package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateDto productCreateDto) {
        final Product newProduct = new Product(productCreateDto.getName(),
            productCreateDto.getPrice());

        return productRepository.save(newProduct);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
