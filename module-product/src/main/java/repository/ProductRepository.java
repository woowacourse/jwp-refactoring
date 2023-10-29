package repository;

import domain.Product;
import exception.ProductException.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    default Product getById(final Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
