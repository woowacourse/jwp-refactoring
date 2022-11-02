package kitchenpos.product.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p.id from Product p where p.id in :ids")
    List<Long> findIdByIds(@Param("ids") List<Long> ids);

    default boolean existsAllById(List<Long> ids) {
        return findIdByIds(ids).size() == ids.size();
    }
}
