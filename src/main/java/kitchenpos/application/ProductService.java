package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dtos.ProductInformationRequest;
import kitchenpos.application.dtos.ProductRequest;
import kitchenpos.domain.MenuProductEvent;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Product create(final ProductRequest request) {
        final Product product = productWith(request);
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    @Transactional
    public Product update(final Long productId, final ProductInformationRequest request) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
        product.updatePrice(request.getPrice());
        eventPublisher.publishEvent(new MenuProductEvent(product.getId(), product.getPrice()));
        productRepository.save(product);
        return product;
    }

    private Product productWith(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .price(new Price(request.getPrice()))
                .build();
    }
}
