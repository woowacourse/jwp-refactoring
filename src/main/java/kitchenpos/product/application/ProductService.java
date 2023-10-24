package kitchenpos.product.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.domain.Money;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductCreateRequest request) {
        Money price = Money.from(request.getPrice());
        Product product = new Product(null, request.getName(), price);
        productRepository.save(product);
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
            .map(ProductResponse::from)
            .collect(toList());
    }
}
