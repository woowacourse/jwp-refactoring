package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.CreateProductCommand;
import kitchenpos.menu.application.dto.CreateProductResponse;
import kitchenpos.menu.application.dto.SearchProductResponse;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public CreateProductResponse create(CreateProductCommand command) {
        Price price = new Price(command.price());
        Product product = new Product(command.name(), price);
        return CreateProductResponse.from(productRepository.save(product));
    }

    public List<SearchProductResponse> list() {
        return productRepository.findAll().stream()
                .map(SearchProductResponse::from)
                .collect(Collectors.toList());
    }
}
