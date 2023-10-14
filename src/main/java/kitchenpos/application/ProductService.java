package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateProductCommand;
import kitchenpos.application.dto.CreateProductResponse;
import kitchenpos.application.dto.SearchProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public CreateProductResponse create(CreateProductCommand command) {
        Price price = new Price(command.price());
        Product product = new Product(command.name(), price);
        Product savedProduct = productDao.save(product);
        return CreateProductResponse.from(savedProduct);
    }

    public List<SearchProductResponse> list() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(SearchProductResponse::from)
                .collect(Collectors.toList());
    }
}
