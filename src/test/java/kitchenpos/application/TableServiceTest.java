package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.ordertable.OrderTableRequest;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;
import kitchenpos.ui.dto.ordertable.OrderTableResponses;

class TableServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("OrderTable 생성")
    @Test
    void create() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        assertAll(
            () -> assertThat(orderTableResponse.getId()).isNotNull(),
            () -> assertThat(orderTableResponse.isEmpty()).isTrue()
        );
    }

    @DisplayName("OrderTable 조회")
    @Test
    void list() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        OrderTableResponses responses = tableService.list();
        List<OrderTableResponse> orderTableResponses = responses.getOrderTableResponses();

        assertAll(
            () -> assertThat(orderTableResponses).hasSize(1),
            () -> assertThat(orderTableResponses.get(0).getId()).isEqualTo(orderTableResponse.getId())
        );
    }

    @DisplayName("OrderTable 비우기 실패 - 유효하지 않는 OrderTableId인 경우")
    @Test
    void changeEmptyFail_When_Invalid_OrderTableId() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);
        tableService.create(orderTableRequest);

        assertThatThrownBy(() -> tableService.changeEmpty(-1L, orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기 실패 - OrderTableGroupId가 이미 있는 경우")
    @Test
    void changeEmptyFail_When_OrderTableGroupId_IsNotNull() {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        orderTable.groupBy(tableGroup);
        orderTableRepository.save(orderTable);

        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기 실패 - 요리 중이거나 식사 중인 경우")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING"})
    void changeEmptyFail_When_OrderTable_Status_CookingOrMeal(OrderStatus orderStatus) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        orderRepository.save(new Order(orderTable, orderStatus, LocalDateTime.now()));
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기")
    @Test
    void changeEmpty() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        orderRepository.save(new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now()));
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        assertAll(
            () -> assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId()),
            () -> assertThat(orderTableResponse.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 인원수 변경 실패 - 유효하지 않은 인원수")
    @Test
    void changeNumberOfGuestsFail_When_InvalidNumberOfGuests() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경 실패 - 유효하지 않은 테이블 아이디")
    @Test
    void changeNumberOfGuestsFail_When_InvalidOrderTableId() {
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, false);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(-1L, orderTableRequest)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경 실패 - 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuestsFail_When_OrderTable_IsEmpty() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true));
        OrderTableRequest orderTableRequest = new OrderTableRequest(2, false);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(3, false));
        OrderTableRequest orderTableRequest = new OrderTableRequest(5, false);

        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTable.getId(),
            orderTableRequest);

        assertAll(
            () -> assertThat(orderTableResponse.getId()).isEqualTo(orderTable.getId()),
            () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests())
        );
    }
}
