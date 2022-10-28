package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableCommand;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("테이블 관련 기능에서")
@SpringBootTest
@ExtendWith(DataClearExtension.class)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("테이블을 정상적으로 생성한다.")
    void create() {
        OrderTableCommand command = new OrderTableCommand(2, true);

        OrderTableResponse response = tableService.create(command);

        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 주문 테이블을 모두 조회한다.")
    void list() {
        orderTableRepository.save(new OrderTable(2, true));
        orderTableRepository.save(new OrderTable(2, true));

        List<OrderTableResponse> responses = tableService.list();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Nested
    @DisplayName("테이블의 상태를 변경할 때")
    class Empty {

        @Test
        @DisplayName("빈 테이블로 변경한다.")
        void changeEmpty() {
            OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true));

            assertThat(response.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블로 변경한다.")
        void changeNotEmpty() {
            OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));

            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, false));

            assertThat(response.isEmpty()).isFalse();
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

//            @Test
//            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
//            void changeEmptyNotFoundTable() {
//                assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableCommand(2, true)))
//                        .hasMessage("주문 테이블이 존재하지 않습니다.");
//            }
//
//            @Test
//            @DisplayName("단체 지정이 되어있는 경우 예외가 발생한다.")
//            void changeEmptyExistGroup() {
//                MenuGroup menuGroup = tableGroupRepository.save(new TableGroup("두마리 메뉴"));
//                OrderTable orderTable = orderTables.get(0);
//
//                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true)))
//                        .hasMessage("이미 단체지정이 되어있습니다.");
//            }

//            @Test
//            @Disabled
//            @DisplayName("조리중인 경우 예외가 발생한다.")
//            void changeEmptyOrderStatusCooking() {
//                OrderLineItem orderLineItem = getOrderLineItem();
//                TableGroup savedTableGroup = saveTableGroup();
//                List<OrderTable> orderTables = savedTableGroup.getOrderTables();
//                OrderTable orderTable = orderTables.get(0);
//
//                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true)))
//                        .hasMessage("조리중이거나 식사 상태입니다.");
//            }
//
//            @Test
//            @Disabled
//            @DisplayName("식사중인 경우 예외가 발생한다.")
//            void changeEmptyOrderStatusCookingMeal() {
//                OrderLineItem orderLineItem = getOrderLineItem();
//
//                TableGroup savedTableGroup = saveTableGroup();
//                List<OrderTable> orderTables = savedTableGroup.getOrderTables();
//                OrderTable orderTable = orderTables.get(0);
//
//                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true)))
//                        .hasMessage("조리중이거나 식사 상태입니다.");
//            }
        }
    }

    @Nested
    @DisplayName("방문한 손님수를 변경할 때")
    class NumberOfGuests {

//        @Test
//        @DisplayName("정상적으로 변경한다.")
//        void changeNumberOfGuests() {
//            Long orderTableId = createOrderTable(2, false).getId();
//
//            ResponseEntity<OrderTable> response = tableController.changeNumberOfGuests(orderTableId,
//                    givenOrderTable(4, false));
//
//            assertThat(response.getBody().getNumberOfGuests()).isEqualTo(4);
//            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
//        }
//
//        @Nested
//        @DisplayName("예외가 발생하는 경우는")
//        class Exception {
//
//            @Test
//            @DisplayName("방문한 손님수가 0 미만인 경우 예외가 발생한다.")
//            void changeNumberOfGuestsLessThanZero() {
//                Long orderTableId = createOrderTable(0, false).getId();
//
//                assertThatThrownBy(() -> tableController.changeNumberOfGuests(orderTableId, givenOrderTable(-1, false)))
//                        .hasMessage("방문한 손님 수는 0 이상이어야 합니다.");
//            }
//
//            @Test
//            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
//            void changeNumberOfGuestsNotFoundGuests() {
//                assertThatThrownBy(() -> tableController.changeNumberOfGuests(1L, givenOrderTable(2, false)))
//                        .hasMessage("주문 테이블이 존재하지 않습니다.");
//            }
//
//            @Test
//            @DisplayName("빈 테이블인 경우 예외가 발생한다.")
//            void changeNumberOfGuestsOrderTableEmpty() {
//                Long orderTableId = createOrderTable(0, true).getId();
//
//                assertThatThrownBy(() -> tableController.changeNumberOfGuests(orderTableId, givenOrderTable(2, true)))
//                        .hasMessage("빈 테이블입니다.");
//            }
//        }
    }
}

