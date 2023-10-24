package kitchenpos.table.application;

import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.order.application.entity.OrderEntity;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.table.application.dto.request.OrderTableReferenceRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.OrderTableQueryResponse;
import kitchenpos.table.application.dto.response.TableGroupQueryResponse;
import kitchenpos.table.application.entity.OrderTableEntity;
import kitchenpos.table.persistence.OrderTableDao;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends ServiceIntegrateTest {

    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private TableGroupService tableGroupService;

    private OrderTableEntity table1;
    private OrderTableEntity table2;
    private OrderTableEntity table3;

    @BeforeEach
    void init() {
        table1 = orderTableDao.findById(1L).get();
        table2 = orderTableDao.findById(2L).get();
        table3 = orderTableDao.findById(3L).get();
    }

    @Test
    @DisplayName("단체 테이블을 등록할 수 있다.")
    void create_success() {
        //given
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));

        //when
        final TableGroupQueryResponse actual = tableGroupService.create(request);
        final Long emptyTableCount = actual.getOrderTables()
                .stream()
                .filter(OrderTableQueryResponse::isEmpty)
                .count();

        //then
        Assertions.assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(emptyTableCount).isZero()
        );

    }

    @Test
    @DisplayName("단체 테이블을 등록할 때 주문 테이블의 수가 2 미만이면 예외를 반환한다.")
    void create_fail_not_multiple_orderTable() {
        //given
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId())
                ));

        //when
        final ThrowingCallable actual = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 등록할 때 존재하지 않는 테이블이 포함되어 있다면 예외를 반환한다.")
    void create_fail_not_exist_orderTable() {
        //given

        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(999L)
                ));

        //when
        final ThrowingCallable actual = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 등록할 때 비어있지 않은 테이블을 포함하고 있으면 예외를 반환한다.")
    void create_fail_not_empty_table() {
        //given
        orderTableDao.save(
                new OrderTableEntity(table1.getId(), null, 0, false));

        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));

        //when
        final ThrowingCallable actual = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 등록할 때 이미 다른 단체 테이블에 포함된 테이블이 포함되어 있으면 예외를 반환한다.")
    void create_fail_already_in_other_tableGroup() {
        //given
        final TableGroupCreateRequest request1 = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));
        final TableGroupCreateRequest request2 = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table2.getId()),
                        new OrderTableReferenceRequest(table3.getId())
                ));
        tableGroupService.create(request1);

        //when
        final ThrowingCallable actual = () -> tableGroupService.create(request2);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 삭제할 수 있다.")
    void ungroup_success() {
        //given
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));
        final Long savedTableGroupId = tableGroupService.create(request).getId();

        //when
        final Executable actual = () -> tableGroupService.ungroup(savedTableGroupId);

        //then
        Assertions.assertDoesNotThrow(actual);
        Assertions.assertAll(
                () -> assertThat(orderTableDao.findById(1L).get().isEmpty()).isFalse(),
                () -> assertThat(orderTableDao.findById(2L).get().isEmpty()).isFalse(),
                () -> assertThat(orderTableDao.findById(1L).get().getTableGroupId()).isNull(),
                () -> assertThat(orderTableDao.findById(2L).get().getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("단체 테이블을 삭제할 때 테이블들의 주문들 중 계산이 완료되지 않은 주문이 있으면 예외를 반환한다.")
    void ungroup_fail_not_COMPLETION_order() {
        //given
        final TableGroupCreateRequest request = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));

        final Long savedTableGroupId = tableGroupService.create(request).getId();

        final OrderEntity order = new OrderEntity(null, table1.getId(),
                OrderStatus.COOKING.name(), LocalDateTime.now());
        orderDao.save(order);

        //when
        final ThrowingCallable actual = () -> tableGroupService.ungroup(savedTableGroupId);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }
}
