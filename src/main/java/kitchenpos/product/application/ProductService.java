package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.ProductEventDto;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCreateRequest product) {
        return ProductResponse.of(productRepository.save(
                new Product(product.getName(), Price.of(product.getPrice()))
        ));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @EventListener
    public void findProduct(ProductEventDto event) {
        Product product = productRepository.getById(event.getId());
        event.setName(product.getName());
        event.setPrice(product.getPrice().getPrice());
    }
}
