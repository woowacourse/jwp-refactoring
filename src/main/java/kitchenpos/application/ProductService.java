package kitchenpos.application;

import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductPrice;
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
    public Product create(final ProductCreateRequest productCreateRequest) {
        String productName = productCreateRequest.getName();
        ProductPrice productPrice = new ProductPrice(productCreateRequest.getPrice());
        
        return productRepository.save(new Product(productName, productPrice));
    }
    
    
    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}
