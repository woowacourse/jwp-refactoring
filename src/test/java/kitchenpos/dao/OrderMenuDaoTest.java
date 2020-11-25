package kitchenpos.dao;

import kitchenpos.domain.OrderMenu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderMenuDaoTest extends DaoTest {

    @DisplayName("전체조회 테스트")
    @Test
    void findAllTest() {
        List<OrderMenu> orderMenus = orderMenuDao.findAll();

        assertAll(
            () -> assertThat(orderMenus).hasSize(2),
            () -> assertThat(orderMenus.get(0)).usingRecursiveComparison().isEqualTo(ORDER_MENU_1),
            () -> assertThat(orderMenus.get(1)).usingRecursiveComparison().isEqualTo(ORDER_MENU_2)
        );
    }

    @DisplayName("단건조회 예외 테스트: id에 해당하는 주문메뉴가 존재하지 않을때")
    @Test
    void findByIdFailByNotExistTest() {
        Optional<OrderMenu> orderMenus = orderMenuDao.findById(-1L);

        assertThat(orderMenus).isEmpty();
    }

    @DisplayName("단건조회 테스트")
    @Test
    void findByIdTest() {
        OrderMenu orderMenu = orderMenuDao.findById(ORDER_MENU_SEQ_1).get();

        assertThat(orderMenu).usingRecursiveComparison().isEqualTo(ORDER_MENU_1);
    }

    @DisplayName("주문id로 전체조회 테스트")
    @Test
    void findAllByOrderIdTest() {
        List<OrderMenu> orderMenus = orderMenuDao.findAllByOrderId(ORDER_ID_1);

        assertAll(
            () -> assertThat(orderMenus).hasSize(1),
            () -> assertThat(orderMenus.get(0)).usingRecursiveComparison().isEqualTo(ORDER_MENU_1)
        );
    }
}