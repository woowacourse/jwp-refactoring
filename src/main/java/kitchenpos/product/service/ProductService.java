package kitchenpos.product.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.CreateProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Product product = new Product(request.getName(), request.getPrice());
        productRepository.save(product);

        return ProductResponse.from(product);
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
