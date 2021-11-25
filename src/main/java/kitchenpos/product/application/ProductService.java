package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final Product product = productRequest.toEntity();
        product.validatePrice();
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }

    public Product findById(Long id) {
        return productDao.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("ProductId에 해당하는 상품이 존재하지 않습니다."));
    }
}
