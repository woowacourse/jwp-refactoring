package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateOrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private Long orderTableId;
    private Long tableGroupId;

    @BeforeEach
    void setUp() {
        tableGroupId = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList())).getId();
        orderTableId = orderTableDao.save(createOrderTable(null, false, tableGroupId, 4)).getId();
    }

    @Test
    @DisplayName("주문 엔티티를 저장하면 id가 부여되고, 엔티티의 필드인 메뉴 상품 리스트는 저장되지 않는다")
    void insert() {
        Order order = createOrder(null, COOKING, orderTableId, LocalDateTime.now());

        Order result = orderDao.save(order);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(result, "id"),
                () -> assertThat(result.getId()).isNotNull()
        );
    }


    @Test
    @DisplayName("존재하는 id로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        Order menuGroup = createOrder(null, COOKING, orderTableId, LocalDateTime.now());
        Order persisted = orderDao.save(menuGroup);

        Order result = orderDao.findById(persisted.getId()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(orderDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        orderDao.save(createOrder(null, COOKING, orderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, MEAL, orderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, COMPLETION, orderTableId, LocalDateTime.now()));

        assertThat(orderDao.findAll()).hasSize(3);
    }

    @ParameterizedTest
    @DisplayName("테이블 id와 테이블 상태 리스트가 주어지면 이에 해당하는 주문이 있는지 여부를 반환한다")
    @MethodSource("generateStatusList")
    void existsByOrderTableIdAndOrderStatusIn(List<String> orderStatuses, boolean expected) {
        Long otherOrderTableId =
                orderTableDao.save(createOrderTable(null, false, tableGroupId, 3)).getId();
        orderDao.save(createOrder(null, COOKING, orderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, COMPLETION, orderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, COOKING, otherOrderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, MEAL, otherOrderTableId, LocalDateTime.now()));

        boolean result = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> generateStatusList() {
        return Stream.of(
                Arguments.of(Arrays.asList(COOKING.name(), MEAL.name()), true),
                Arguments.of(Arrays.asList(COOKING.name()), true),
                Arguments.of(Arrays.asList(MEAL.name()), false),
                Arguments.of(Arrays.asList(COOKING.name(), COMPLETION.name()), true),
                Arguments.of(Arrays.asList(COOKING.name(), MEAL.name(), COMPLETION.name()), true)
        );
    }

    @ParameterizedTest
    @DisplayName("테이블 id 리스트와 테이블 상태 리스트가 주어지면 이에 해당하는 주문이 있는지 여부를 반환한다")
    @MethodSource("generateStatusList2")
    void existsByOrderTableInIdAndOrderStatusIn(List<String> orderStatuses, boolean expected) {
        Long otherOrderTableId =
                orderTableDao.save(createOrderTable(null, false, tableGroupId, 3)).getId();
        Long anotherOrderTableId =
                orderTableDao.save(createOrderTable(null, false, tableGroupId, 3)).getId();
        orderDao.save(createOrder(null, COOKING, orderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, COMPLETION, anotherOrderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, COOKING, otherOrderTableId, LocalDateTime.now()));
        orderDao.save(createOrder(null, MEAL, otherOrderTableId, LocalDateTime.now()));

        boolean result = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTableId, anotherOrderTableId),
                orderStatuses
        );
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> generateStatusList2() {
        return Stream.of(
                Arguments.of(Arrays.asList(COOKING.name(), MEAL.name()), true),
                Arguments.of(Arrays.asList(COOKING.name()), true),
                Arguments.of(Arrays.asList(MEAL.name()), false),
                Arguments.of(Arrays.asList(COOKING.name(), COMPLETION.name()), true),
                Arguments.of(Arrays.asList(COOKING.name(), MEAL.name(), COMPLETION.name()), true)
        );
    }
}