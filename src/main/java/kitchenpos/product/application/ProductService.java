package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.exception.ProductPriceMoreLessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest req) {
        Product updateProduct = req.toDomain();

        return productRepository.save(updateProduct);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void validateProductSize(final List<Long> productIds) {
        int productSize = productIds.size();
        long savedProductSize = productRepository.countAllByIdIn(productIds);

        if (savedProductSize != productSize) {
            throw new ProductNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public void validateProductPriceLess(final List<Long> productIds, final Long menuPrice) {
        Price productPriceSum = productRepository.sumPricesByIdIn(productIds);

        if (productPriceSum.getPrice() < menuPrice) {
            throw new ProductPriceMoreLessException();
        }
    }
}
