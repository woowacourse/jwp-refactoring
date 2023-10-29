package kitchenpos.product.application;

import kitchenpos.common.vo.Price;
import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest nameAndPrice) {
        final Product product = new Product(nameAndPrice.getName(), new Price(nameAndPrice.getPrice()));
        return ProductResponse.from(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
