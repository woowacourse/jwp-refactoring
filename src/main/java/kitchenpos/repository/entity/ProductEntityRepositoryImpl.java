package kitchenpos.repository.entity;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;
import org.springframework.context.annotation.Lazy;

public class ProductEntityRepositoryImpl implements ProductEntityRepository {

    private final ProductRepository productRepository;

    @Lazy
    public ProductEntityRepositoryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllByIdIn(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품이 있습니다 : " + productIds);
        }
        return products;
    }
}
