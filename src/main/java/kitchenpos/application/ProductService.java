package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), new Price(request.getPrice()));
        final Product saveProduct = productRepository.save(product);

        return saveProduct.getId();
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
