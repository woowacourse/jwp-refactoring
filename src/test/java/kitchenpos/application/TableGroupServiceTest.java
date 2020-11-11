package kitchenpos.application;

import static kitchenpos.helper.OrderHelper.*;
import static kitchenpos.helper.OrderTableHelper.*;
import static kitchenpos.helper.TableGroupHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    private static Stream<Arguments> provideOrderStatus() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING),
                Arguments.of(OrderStatus.MEAL)
        );
    }

    @BeforeEach
    void setUp() {
        주문_테이블_1 = orderTableDao.save(createOrderTable(null, 0, true));
        주문_테이블_2 = orderTableDao.save(createOrderTable(null, 3, true));
    }

    @DisplayName("테이블에 단체 지정을 할 수 있다.")
    @Test
    void create() {
        // given
        TableGroup 생성할_테이블_그룹 = createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        TableGroup 테이블_그룹 = tableGroupService.create(생성할_테이블_그룹);

        // then
        assertAll(
                () -> assertThat(테이블_그룹.getId()).isNotNull(),
                () -> assertThat(테이블_그룹.getCreatedDate()).isEqualTo(생성할_테이블_그룹.getCreatedDate()),
                () -> assertThat(테이블_그룹.getOrderTables().get(0).getTableGroupId()).isEqualTo(테이블_그룹.getId()),
                () -> assertThat(테이블_그룹.getOrderTables().get(1).getTableGroupId()).isEqualTo(테이블_그룹.getId())
        );
    }

    @DisplayName("단체 지정 시 주문 테이블이 2개 미만인 경우 예외가 발생한다.")
    @Test
    void create_OrderTableUnderTwo_ExceptionThrown() {
        // given
        TableGroup 생성할_테이블_그룹 = createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(생성할_테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 존재하지 않는 테이블을 입력한 경우 예외가 발생한다.")
    @Test
    void create_UnregisteredOrderTable_ExceptionThrown() {
        // given
        OrderTable 등록되지_않은_주문_테이블 = createOrderTable(null, 0, true);
        TableGroup 생성할_테이블_그룹 = createTableGroup(LocalDateTime.now(),
                Arrays.asList(주문_테이블_1, 등록되지_않은_주문_테이블));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(생성할_테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 비어있지 않은 테이블을 입력한 경우 예외가 발생한다.")
    @Test
    void create_TableIsNotEmpty_ExceptionThrown() {
        // given
        OrderTable 비어있지_않은_주문_테이블 = createOrderTable(null, 0, false);
        TableGroup 생성할_테이블_그룹 = createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1, 비어있지_않은_주문_테이블));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(생성할_테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 이미 단체 지정된 테이블을 입력한 경우 예외가 발생한다.")
    @Test
    void create_AlreadyGroupedTableExist_ExceptionThrown() {
        // given
        TableGroup 생성된_테이블_그룹 = tableGroupService.create(
                createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1, 주문_테이블_2)));

        OrderTable 주문_테이블_3 = orderTableDao.save(createOrderTable(null, 2, true));
        TableGroup 생성할_테이블_그룹 = createTableGroup(LocalDateTime.now(),
                Arrays.asList(주문_테이블_3, 생성된_테이블_그룹.getOrderTables().get(0)));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(생성할_테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        TableGroup 생성된_테이블_그룹 = tableGroupService.create(
                createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1, 주문_테이블_2)));

        // when
        tableGroupService.ungroup(생성된_테이블_그룹.getId());

        // then
        assertThat(orderTableDao.findById(주문_테이블_1.getId()).get().getTableGroupId()).isNull();
        assertThat(orderTableDao.findById(주문_테이블_2.getId()).get().getTableGroupId()).isNull();
    }

    @DisplayName("단체 지정을 해제할 때 이미 조리중이거나 식사중인 주문이 있는 경우 예외가 발생한다.")
    @MethodSource("provideOrderStatus")
    @ParameterizedTest
    void ungroup(OrderStatus orderStatus) {
        // given
        TableGroup 생성된_테이블_그룹 = tableGroupService.create(
                createTableGroup(LocalDateTime.now(), Arrays.asList(주문_테이블_1, 주문_테이블_2)));
        orderDao.save(createOrder(주문_테이블_1.getId(), orderStatus.name(), LocalDateTime.now(),
                Collections.emptyList()));

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(생성된_테이블_그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
