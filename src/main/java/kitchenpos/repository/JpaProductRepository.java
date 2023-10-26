package kitchenpos.repository;

import static kitchenpos.exception.ProductExceptionType.NOT_EXIST_PRODUCT_EXCEPTION;

import kitchenpos.domain.Product;
import kitchenpos.exception.ProductException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

    default Product getById(Long id) {
        return findById(id).orElseThrow(() -> new ProductException(NOT_EXIST_PRODUCT_EXCEPTION));
    }
}
