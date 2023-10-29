package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.domain.repository.ProductRepository;
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
        final Product product = new Product(
                new ProductName(request.getName()),
                new ProductPrice(request.getPrice())
        );
        final Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse convertToResponse(final Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
