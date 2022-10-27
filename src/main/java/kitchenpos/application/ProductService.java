package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductCreateRequest;
import kitchenpos.ui.dto.ProductResponse;
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
        final Product product = new Product(request.getName(), request.getPrice());
        productRepository.save(product);

        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(it -> new ProductResponse(it.getId(), it.getName(), it.getPrice()))
                .collect(Collectors.toList());
    }
}
