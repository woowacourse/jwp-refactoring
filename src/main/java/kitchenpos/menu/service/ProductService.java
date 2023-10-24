package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Product;
import kitchenpos.value.Price;
import kitchenpos.menu.dto.request.CreateProductRequest;
import kitchenpos.menu.dto.response.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
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
