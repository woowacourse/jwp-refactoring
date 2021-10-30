package kitchenpos.application;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product newProduct = new Product.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(newProduct);

        return ProductResponse.of(newProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.toList(products);
    }
}
