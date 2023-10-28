package kitchenpos.product.apllication;

import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCreateRequest request) {
        Product newProduct = productRepository.save(
            new Product(null, request.getName(), new Price(request.getPrice())));
        return ProductResponse.of(newProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.of(productRepository.findAll());
    }
}
