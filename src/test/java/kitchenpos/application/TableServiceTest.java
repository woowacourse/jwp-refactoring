package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyEditRequest;
import kitchenpos.table.dto.TableGuestEditRequest;
import kitchenpos.table.service.TableService;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("주문테이블 추가한다.")
    @Test
    void create() {
        TableCreateRequest request = new TableCreateRequest(1, true);

        Long id = tableService.create(request);

        assertThat(id).isNotNull();
    }

    @DisplayName("주문테이블 리스트를 조회한다.")
    @Test
    void list() {
        //todo 이게 맞는거냐>?
        orderTableRepository.deleteAll();

        TableCreateRequest request1 = new TableCreateRequest(1, true);
        Long requestOneId = tableService.create(request1);
        TableCreateRequest request2 = new TableCreateRequest(1, true);
        Long requestTwoId = tableService.create(request2);

        List<Table> actual = tableService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual.get(0).getId()).isEqualTo(requestOneId),
            () -> assertThat(actual.get(1).getId()).isEqualTo(requestTwoId)
        );
    }

    @DisplayName("주문테이블이 없는경우에 테이블의 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenNoOrderTable() {
        TableEmptyEditRequest request = new TableEmptyEditRequest(true);
        assertThatThrownBy(
            () -> tableService.editEmpty(1L, request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체가 지정되어 있는 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenHasTableGroupId() {
        Table saved1 = orderTableRepository.save(createTable(null, true, null, 3));
        Table saved2 = orderTableRepository.save(createTable(null, true, null, 3));
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(Arrays.asList(saved1, saved2)));
        System.out.println(savedTableGroup.getCreatedDate().toString());

        Table table = createTable(null, true, savedTableGroup, 1);
        Table savedTable = orderTableRepository.save(table);

        assertThatThrownBy(() -> tableService.editEmpty(savedTable.getId(), new TableEmptyEditRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 들어가지 않거나, 계산이 완료되지 않은 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenOrderStatusIsNullOrCompletion() {
        Table table = createTable(null, false, null, 1);
        Table savedTable = orderTableRepository.save(table);

        Order order = createOrder(null, Collections.emptyList(), OrderStatus.COOKING,
            savedTable);
        orderRepository.save(order);

        assertThatThrownBy(() -> tableService.editEmpty(savedTable.getId(), new TableEmptyEditRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 사람의 유무 여부를 변경한다")
    @Test
    void changeEmpty() {
        Table table = createTable(null, false, null, 1);
        Table savedTable = orderTableRepository.save(table);
        boolean expect = false;

        Order order = createOrder(null, Collections.emptyList(), OrderStatus.COMPLETION, savedTable);
        orderRepository.save(order);

        tableService.editEmpty(savedTable.getId(), new TableEmptyEditRequest(expect));

        Table actual = orderTableRepository.findById(savedTable.getId()).get();

        assertThat(actual.isEmpty()).isEqualTo(expect);
    }

    @DisplayName("변경하려는 손님의 수가 0미만일 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNumberIsBelowZero() {
        TableGuestEditRequest request = new TableGuestEditRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNoOrderTable() {
        Table table = createTable(null, true, null, 3);
        Table savedTable = orderTableRepository.save(table);

        TableGuestEditRequest request = new TableGuestEditRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문테이블의 착석한 손님이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenIsEmpty() {
        Table table = createTable(null, true, null, 1);
        Table savedTable = orderTableRepository.save(table);

        TableGuestEditRequest request = new TableGuestEditRequest(2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        Table table = createTable(null, false, null, 1);
        Table savedTable = orderTableRepository.save(table);

        TableGuestEditRequest request = new TableGuestEditRequest(4);

        tableService.changeNumberOfGuests(savedTable.getId(), request);
    }
}
