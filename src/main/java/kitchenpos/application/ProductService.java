package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest productCreateRequest) {
        Long price = productCreateRequest.getPrice();
        String name = productCreateRequest.getName();

        Product product = productDao.save(new Product(name, price));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(each -> new ProductResponse(each.getId(), each.getName(), each.getPrice()))
                .collect(Collectors.toUnmodifiableList());
    }
}
