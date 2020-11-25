package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.exception.NullRequestException;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreateRequest productCreateRequest) {
        validateProductCreateRequest(productCreateRequest);

        String name = productCreateRequest.getName();
        Price price = Price.of(new BigDecimal(productCreateRequest.getPrice()));
        return productDao.save(new Product(name, price));
    }

    private void validateProductCreateRequest(ProductCreateRequest productCreateRequest) {
        String name = productCreateRequest.getName();
        Long price = productCreateRequest.getPrice();

        if (Objects.isNull(price) || Objects.isNull(name)) {
            throw new NullRequestException();
        }
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
