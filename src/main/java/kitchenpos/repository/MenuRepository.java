package kitchenpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
