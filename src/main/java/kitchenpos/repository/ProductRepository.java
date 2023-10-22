package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(final Collection<Long> id);
}
