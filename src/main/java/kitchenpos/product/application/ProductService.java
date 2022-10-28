package kitchenpos.product.application;

import kitchenpos.product.application.dto.ProductRequestDto;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.domain.Product;
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
    public Product create(final ProductRequestDto productRequestDto) {
        return productRepository.save(new Product(productRequestDto.getName(), productRequestDto.getPrice()));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
