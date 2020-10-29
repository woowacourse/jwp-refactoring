package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuProductDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<MenuProduct> menuProducts = menuProductDao.findAll();

        assertAll(
            () -> assertThat(menuProducts).hasSize(2),
            () -> assertThat(menuProducts.get(0)).usingRecursiveComparison().isEqualTo(MENU_PRODUCT_1),
            () -> assertThat(menuProducts.get(1)).usingRecursiveComparison().isEqualTo(MENU_PRODUCT_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 메뉴가 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<MenuProduct> menuProduct = menuProductDao.findById(-1L);

        assertThat(menuProduct).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        MenuProduct menuProduct = menuProductDao.findById(MENU_PRODUCT_SEQ_1).get();

        assertThat(menuProduct).usingRecursiveComparison().isEqualTo(MENU_PRODUCT_1);
    }

    @DisplayName("메뉴id로 전체조회 테스트")
    @Test
    void findAllByMenuIdTest() {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(MENU_ID_1);

        assertAll(
            () -> assertThat(menuProducts).hasSize(1),
            () -> assertThat(menuProducts.get(0)).usingRecursiveComparison().isEqualTo(MENU_PRODUCT_1)
        );
    }
}