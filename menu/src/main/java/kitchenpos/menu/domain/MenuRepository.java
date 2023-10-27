package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByNameAndPrice(MenuName name, MenuPrice price);

}
