package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.CreateProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final CreateProductRequest request) {
        final Product product = productRepository.save(
            new Product(request.getName(), request.getPrice())
        );

        return new ProductResponse(product);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }

}

