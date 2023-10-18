package kitchenpos.application;

import kitchenpos.application.dto.request.CreateProductRequest;
import kitchenpos.application.dto.response.CreateProductResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public CreateProductResponse create(final CreateProductRequest request) {
        Product product = ProductMapper.toProduct(request);
        Product entity = productDao.save(product);
        return CreateProductResponse.from(entity);
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
