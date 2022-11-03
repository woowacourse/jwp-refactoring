package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {
        return toResponse(productDao.save(toProduct(product)));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPriceValue());
    }

    private Product toProduct(ProductRequest product) {
        return new Product(product.getName(), new Price(product.getPrice()));
    }
}
