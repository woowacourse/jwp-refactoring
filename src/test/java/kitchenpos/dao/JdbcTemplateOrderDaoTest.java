package kitchenpos.dao;

import static java.util.Arrays.*;
import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import(JdbcTemplateOrderDao.class)
@Sql("/data-for-dao.sql")
class JdbcTemplateOrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    private static Stream<Arguments> orderTableIdAndOrderStatusInTestSet() {
        return Stream.of(
                Arguments.arguments(1L, asList("MEAL", "COOKING"), true),
                Arguments.arguments(2L, asList("MEAL", "COOKING"), true),
                Arguments.arguments(1L, asList("COMPLETED", "COOKING"), false),
                Arguments.arguments(2L, asList("COMPLETED", "COOKING"), true)
        );
    }

    private static Stream<Arguments> orderTableIdInAndOrderStatusInTestSet() {
        return Stream.of(
                Arguments.arguments(asList(1L, 2L), asList("MEAL", "COOKING"), true),
                Arguments.arguments(asList(1L, 2L), asList("MEAL", "COOKING"), true),
                Arguments.arguments(asList(1L, 2L), asList("COMPLETED"), false)
        );
    }

    @DisplayName("Order entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 id를 가져온다.")
    @Test
    void save() {
        Order 새주문 = orderDao.save(createOrder(1L, OrderStatus.COOKING));
        assertThat(새주문.getId()).isNotNull();
    }

    @DisplayName("존재하는 Order Id로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<Order> 존재하는메뉴상품 = orderDao.findById(1L);
        assertThat(존재하는메뉴상품).isNotEmpty();
    }

    @DisplayName("존재하지 않는 Order Id로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<Order> 존재하지않는메뉴상품 = orderDao.findById(9999L);
        assertThat(존재하지않는메뉴상품).isEmpty();
    }

    @DisplayName("database에 존재하는 Order 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<Order> actual = orderDao.findAll();
        assertThat(actual).hasSize(2);
    }

    @DisplayName("전체 주문 목록 중, 특정 Table Id를 가지고 있고, 주문 상태 목록 중 하나에 속하는 목록을 가져온다.")
    @ParameterizedTest
    @MethodSource("orderTableIdAndOrderStatusInTestSet")
    void existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatus, boolean expected) {
        boolean actual = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatus);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("전체 주문 목록 중, Table Id목록 중 하나에 속하고, 주문 상태 목록 중 하나에 속하는 목록을 가져온다.")
    @ParameterizedTest
    @MethodSource("orderTableIdInAndOrderStatusInTestSet")
    void existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatus, boolean expected) {
        boolean actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatus);
        assertThat(actual).isEqualTo(expected);
    }
}