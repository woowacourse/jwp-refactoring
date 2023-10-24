package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.vo.product.ProductRequest;
import kitchenpos.vo.product.ProductResponse;
import kitchenpos.vo.product.ProductsResponse;
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
