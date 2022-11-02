package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.application.dto.request.ProductRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Long create(ProductRequest productRequest) {
        return productDao.save(new Product(productRequest.getName(), productRequest.getPrice()));
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice()))
                .collect(Collectors.toList());
    }
}
