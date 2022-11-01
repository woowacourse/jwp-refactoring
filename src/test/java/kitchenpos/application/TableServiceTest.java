package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.ChangeNumOfTableGuestsRequest;
import kitchenpos.application.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.support.SpringBootNestedTest;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("테이블 정보를 생성하면 ID가 할당된 OrderTable객체가 반환된다")
    @Test
    void create() {
        OrderTableRequest request = new OrderTableRequest(3, false);

        OrderTableResponse actual = tableService.create(request);
        assertThat(actual).isNotNull();
    }

    @DisplayName("존재하는 모든 테이블 목록을 조회한다")
    @Test
    void list() {
        int numOfTables = 3;
        for (int i = 0; i < numOfTables; i++) {
            OrderTable orderTable = new OrderTable(3, false);
            orderTableRepository.save(orderTable);
        }

        List<OrderTableResponse> actual = tableService.list();
        assertThat(actual).hasSize(3);
    }

    @DisplayName("테이블을 비어있는 테이블로 설정한다")
    @SpringBootNestedTest
    class ChangeEmptyTest {

        ChangeOrderTableEmptyRequest changeOrderTableEmptyRequest = new ChangeOrderTableEmptyRequest(true);

        @DisplayName("테이블을 비어있는 테이블로 설정한다")
        @Test
        void changeEmpty() {
            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableRepository.save(newOrderTable);

            orderRepository.save(new Order(orderTable, OrderStatus.COMPLETION));

            OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest);
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("존재하지 않는 테이블일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistOrderTable() {
            Long notExistId = 0L;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistId, changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 테이블입니다.");
        }

        @DisplayName("테이블이 다른 테이블 그룹에 묶여있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseTableBelongsToTableGroup() {
            TableGroup newTableGroup = new TableGroup();
            TableGroup tableGroup = tableGroupRepository.save(newTableGroup);

            OrderTable newOrderTable = new OrderTable(3, true);
            newOrderTable.joinTableGroup(tableGroup);
            OrderTable orderTable = orderTableRepository.save(newOrderTable);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 테이블 그룹이 형성된 테이블입니다.");
        }

        @DisplayName("테이블 중 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableRepository.save(newOrderTable);

            orderRepository.save(new Order(orderTable, OrderStatus.COOKING));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTableEmptyRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("비울 수 없는 테이블이 존재합니다.");
        }
    }

    @DisplayName("손님 수를 변경한다")
    @SpringBootNestedTest
    class ChangeNumberOfGuestsTest {

        OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable newOrderTable = new OrderTable(3, false);
            newOrderTable.changeEmptyStatus(false);
            orderTable = orderTableRepository.save(newOrderTable);
        }

        @DisplayName("손님수를 올바르게 변경한다")
        @Test
        void changeNumberOfGuests() {
            int numberOfGuests = 10;
            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(numberOfGuests);

            OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsRequest);
            assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
        }

        @DisplayName("변경하려는 손님 수가 음수일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNegativeNumberOfGuests() {
            int numberOfGuests = -1;

            OrderTable newOrderTable = new OrderTable(3, false);
            OrderTable orderTable = orderTableRepository.save(newOrderTable);

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(numberOfGuests);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블에는 0명 이상의 손님이 앉을 수 있습니다.");
        }

        @DisplayName("존재하지 않는 테이블 Id일 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistGroupTable() {
            Long notExistId = 0L;

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistId, changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 테이블입니다.");
        }

        @DisplayName("테이블이 비어있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable(10, true);
            OrderTable emptyTable = orderTableRepository.save(newEmptyTable);

            ChangeNumOfTableGuestsRequest changeGuestsRequest = new ChangeNumOfTableGuestsRequest(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(emptyTable.getId(), changeGuestsRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주문 테이블이 비어있습니다.");
        }
    }
}
