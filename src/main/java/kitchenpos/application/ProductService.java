package kitchenpos.application;

import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.ProductMapper;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateProductResponse create(final CreateProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        Product entity = productRepository.save(product);
        return CreateProductResponse.from(entity);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
