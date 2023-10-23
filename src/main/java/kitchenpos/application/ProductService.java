package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;
import kitchenpos.domain.ProductPrice;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
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
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = new Product(
                new ProductName(productRequest.getName()),
                new ProductPrice(productRequest.getPrice())
        );
        return convertToResponse(productRepository.save(product));
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
