package kitchenpos.product.application;

import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.application.response.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCreateRequest productCreateRequest) {
        Product product = Product.of(productCreateRequest.getName(), productCreateRequest.getPrice());
        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
