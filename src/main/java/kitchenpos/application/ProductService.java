package kitchenpos.application;

import kitchenpos.application.request.product.ProductRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.product.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductDao productDao;
    private final ResponseAssembler responseAssembler;

    public ProductService(final ProductDao productDao, final ResponseAssembler responseAssembler) {
        this.productDao = productDao;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final BigDecimal price = request.getPrice();
        validatePriceNotNegative(price);

        final var product = new Product(request.getName(), request.getPrice());
        final var savedProduct =  productDao.save(product);

        return responseAssembler.productResponse(savedProduct);
    }

    private void validatePriceNotNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<ProductResponse> list() {
        final var products = productDao.findAll();
        return responseAssembler.productResponses(products);
    }
}
