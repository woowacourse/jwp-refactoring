package kitchenpos.application;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.dto.product.ProductsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {

        Product product = new Product(productRequest.getName(), productRequest.getPrice());

        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public ProductsResponse list() {
        return ProductsResponse.of(productRepository.findAll());
    }
}
