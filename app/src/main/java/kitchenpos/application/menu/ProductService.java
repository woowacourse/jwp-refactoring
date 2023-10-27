package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.CreateProductCommand;
import kitchenpos.application.menu.dto.CreateProductResponse;
import kitchenpos.application.menu.dto.SearchProductResponse;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.ProductRepository;
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
