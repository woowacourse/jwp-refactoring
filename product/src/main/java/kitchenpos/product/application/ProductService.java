package kitchenpos.product.application;

import kitchenpos.common.domain.Money;
import kitchenpos.common.domain.Name;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.CreateProductRequest;
import kitchenpos.product.dto.ListProductResponse;
import kitchenpos.product.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final CreateProductRequest request) {
        final Product product = Product.of(Name.of(request.getName()), Money.valueOf(request.getPrice()));
        return ProductResponse.from(productRepository.save(product));
    }

    public ListProductResponse list() {
        return ListProductResponse.from(productRepository.findAll());
    }
}
