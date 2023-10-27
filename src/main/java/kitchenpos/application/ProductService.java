package kitchenpos.application;

import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.persistence.ProductRepository;
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

        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getPrice().getValue());
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice().getValue()))
                .collect(Collectors.toList());
    }
}
