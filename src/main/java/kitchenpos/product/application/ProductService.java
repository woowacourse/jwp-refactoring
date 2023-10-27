package kitchenpos.product.application;

import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductQueryResponse;
import kitchenpos.product.domain.Product;
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
    public ProductQueryResponse create(final ProductCreateRequest request) {
        final Product product = request.toProduct();
        return ProductQueryResponse.from(productRepository.save(product));
    }

    public List<ProductQueryResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductQueryResponse(product.getId(), product.getName(),
                        product.getPrice().getValue()))
                .collect(Collectors.toList());
    }
}
