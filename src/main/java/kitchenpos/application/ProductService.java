package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;

@Service
@Transactional
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = productDao.save(request.toProduct());

        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();

        return products.stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());
    }
}
