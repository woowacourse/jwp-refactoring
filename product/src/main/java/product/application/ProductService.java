package product.application;

import common.domain.Money;
import common.domain.Name;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.domain.Product;
import product.domain.ProductRepository;
import product.dto.CreateProductRequest;
import product.dto.ListProductResponse;
import product.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final CreateProductRequest request) {
        final Product product = Product.of(Name.of(request.getName()), Money.valueOf(request.getPrice()));
        return ProductResponse.from(productRepository.save(product));
    }

    public ListProductResponse list() {
        return ListProductResponse.from(productRepository.findAll());
    }
}
