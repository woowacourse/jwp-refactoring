package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.domain.Products;
import kitchenpos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public Products findAllByIdIn(List<Long> ids) {
        List<Product> products = productRepository.findAllByIdIn(ids);
        return new Products(products);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
