package kitchenpos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuProduct;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {

    List<MenuProduct> findAllByMenuId(Long menuId);

}
