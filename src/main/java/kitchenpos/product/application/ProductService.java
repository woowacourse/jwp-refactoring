package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.common.dto.request.ProductCreationRequest;
import kitchenpos.common.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductCreationRequest request) {
        Product product = Product.create(request.getName(), request.getPrice());

        productRepository.save(product);

        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

}
