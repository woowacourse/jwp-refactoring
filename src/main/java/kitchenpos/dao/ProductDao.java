package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Long> {
    List<Product> findAll();

    default Product findMandatoryById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
