package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.mapper.ProductMapper;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            final ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(
            final ProductCreateRequest request
    ) {
        final Product product = Product.of(request.getName(), request.getPrice());
        final Product savedProduct = productRepository.save(product);

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> readAll() {
        final List<Product> products = productRepository.findAll();

        return ProductMapper.toProductResponses(products);
    }
}
