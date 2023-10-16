package kitchenpos.application;

import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public CreateProductResponse create(final CreateProductRequest request) {
        final BigDecimal price = new BigDecimal(request.getPrice());

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product product = ProductMapper.toProduct(request);
        Product entity = productDao.save(product);

        return CreateProductResponse.from(entity);
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
