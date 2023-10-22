package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        Product newProduct = productRepository.save(
            new Product(null, request.getName(), new Price(request.getPrice())));
        return ProductResponse.of(newProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.of(productRepository.findAll());
    }
}
