package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
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
    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(new Name(request.getName()), new Price(request.getPrice()));
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.from(productRepository.findAll());
    }
}
