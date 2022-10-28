package kitchenpos.application;

import kitchenpos.application.dto.convertor.ProductConvertor;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = ProductConvertor.toProduct(request);
        final Product savedProduct = productDao.save(product);
        return ProductConvertor.toProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productDao.findAll();
        return ProductConvertor.toProductResponses(products);
    }
}
