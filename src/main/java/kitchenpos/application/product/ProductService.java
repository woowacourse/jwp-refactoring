package kitchenpos.application;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.common.Name;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.CreateProductRequest;
import kitchenpos.dto.product.ListProductResponse;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
