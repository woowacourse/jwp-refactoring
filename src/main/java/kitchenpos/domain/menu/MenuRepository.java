package kitchenpos.domain.menu;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Long> {
    //    Menu save(Menu entity);
//
//    Optional<Menu> findById(Long id);
//
    List<Menu> findAll();

    //
    long countByIdIn(List<Long> ids);
}
