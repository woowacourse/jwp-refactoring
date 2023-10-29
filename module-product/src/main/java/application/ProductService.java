package application;

import application.dto.ProductCreateRequest;
import application.dto.ProductResponse;
import domain.Product;
import domain.ProductName;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ProductRepository;
import vo.Price;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final ProductName productName = new ProductName(request.getName());
        final Price price = new Price(request.getPrice());
        final Product product = productRepository.save(new Product(productName, price));

        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
