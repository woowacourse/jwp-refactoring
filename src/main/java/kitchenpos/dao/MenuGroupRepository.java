package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}
