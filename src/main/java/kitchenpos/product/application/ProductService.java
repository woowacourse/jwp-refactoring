package kitchenpos.product.application;

import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.application.mapper.ProductMapper;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.persistence.ProductRepository;
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
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), new Price(request.getPrice()));
        final Product savedProduct = productRepository.save(product);

        return ProductMapper.mapToProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
