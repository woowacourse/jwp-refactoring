package kitchenpos.application;

import static kitchenpos.helper.OrderHelper.*;
import static kitchenpos.helper.OrderTableHelper.*;
import static kitchenpos.helper.TableGroupHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Sql("/truncate.sql")
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private static Stream<Arguments> provideOrderStatus() {
        return Stream.of(
                Arguments.of(OrderStatus.COOKING),
                Arguments.of(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderTable 생성할_주문_테이블 = createOrderTable(null, 0, true);

        // when
        OrderTable 주문_테이블 = tableService.create(생성할_주문_테이블);

        // then
        assertAll(
                () -> assertThat(주문_테이블.getId()).isNotNull(),
                () -> assertThat(주문_테이블.getTableGroupId()).isNull(),
                () -> assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(생성할_주문_테이블.getNumberOfGuests()),
                () -> assertThat(주문_테이블.getTableGroupId()).isEqualTo(생성할_주문_테이블.getTableGroupId())
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        OrderTable 주문_테이블_1 = tableService.create(createOrderTable(null, 0, true));
        OrderTable 주문_테이블_2 = tableService.create(createOrderTable(null, 0, true));

        // when
        List<OrderTable> 주문_테이블_목록 = tableService.list();

        // then
        assertAll(
                () -> assertThat(주문_테이블_목록).hasSize(2),
                () -> assertThat(주문_테이블_목록.get(0).getId()).isEqualTo(주문_테이블_1.getId()),
                () -> assertThat(주문_테이블_목록.get(1).getId()).isEqualTo(주문_테이블_2.getId())
        );
    }

    @DisplayName("빈 주문 테이블로 설정 또는 해지할 수 있다.")
    @ValueSource(booleans = {false, true})
    @ParameterizedTest
    void changeEmpty(boolean empty) {
        // given
        OrderTable 주문_테이블 = tableService.create(createOrderTable(null, 0, empty));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, 0, !empty);

        // when
        tableService.changeEmpty(주문_테이블.getId(), 변경할_주문_테이블_상태);
        OrderTable 설정_변경된_테이블 = orderTableDao.findById(주문_테이블.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(설정_변경된_테이블.isEmpty()).isEqualTo(변경할_주문_테이블_상태.isEmpty());
    }

    @DisplayName("단체 지정된 경우 빈 테이블로 설정 또는 해지 시도시 예외가 발생한다.")
    @ValueSource(booleans = {false, true})
    @ParameterizedTest
    void changeEmpty_isGroupedTable_ExceptionThrown(boolean empty) {
        // given
        TableGroup 테이블_그룹 = tableGroupDao.save(createTableGroup(LocalDateTime.now(), Collections.emptyList()));
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(테이블_그룹.getId(), 0, empty));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, 0, !empty);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 변경할_주문_테이블_상태))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 주문 상태인 경우 설정을 변경할 수 없다.")
    @MethodSource("provideOrderStatus")
    @ParameterizedTest
    void changeEmpty_AlreadyOrderedStatus_ExceptionThrown(OrderStatus orderStatus) {
        // given
        boolean isTableEmpty = false;
        TableGroup 테이블_그룹 = tableGroupDao.save(createTableGroup(LocalDateTime.now(), Collections.emptyList()));
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(테이블_그룹.getId(), 0, isTableEmpty));
        Order 주문 = orderDao.save(
                createOrder(주문_테이블.getId(), orderStatus.name(), LocalDateTime.now(), Collections.emptyList()));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, 0, !isTableEmpty);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 변경할_주문_테이블_상태))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님의 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 1, false));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, 0, false);

        // when
        tableService.changeNumberOfGuests(주문_테이블.getId(), 변경할_주문_테이블_상태);
        OrderTable 설정_변경된_테이블 = orderTableDao.findById(주문_테이블.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(설정_변경된_테이블.getNumberOfGuests()).isEqualTo(변경할_주문_테이블_상태.getNumberOfGuests());
    }

    @DisplayName("방문한 손님의 수 변경 시 변경하려는 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_ChangeNumberOfGuestUnderZero_ExceptionThrown() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, false));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, -1, false);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), 변경할_주문_테이블_상태))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님의 수 변경 시 주문 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_OrderTableIsEmpty_ExceptionThrown() {
        // given
        OrderTable 주문_테이블 = orderTableDao.save(createOrderTable(null, 0, true));
        OrderTable 변경할_주문_테이블_상태 = createOrderTable(null, 1, true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), 변경할_주문_테이블_상태))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
