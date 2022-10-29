package kitchenpos.application;

import kitchenpos.application.request.product.ProductRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.product.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        final var price = new Price(request.getPrice());

        final var product = asProduct(request);
        final var savedProduct =  productDao.save(product);

        return responseAssembler.productResponse(savedProduct);
    }

    private Product asProduct(final ProductRequest request) {
        final var product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return product;
    }

    public List<ProductResponse> list() {
        final var products = productDao.findAll();
        return responseAssembler.productResponses(products);
    }
}
