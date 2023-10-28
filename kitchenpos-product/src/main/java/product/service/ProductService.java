package product.service;

import java.util.List;
import java.util.stream.Collectors;
import product.domain.Product;
import product.dto.request.CreateProductRequest;
import product.dto.response.ProductResponse;
import product.repository.ProductRepository;
import value.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final CreateProductRequest request) {
        final Price price = new Price(request.getPrice());

        final Product product = new Product(request.getName(), price);

        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
