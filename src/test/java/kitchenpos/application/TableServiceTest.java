package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.OrderTableCommand;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.common.DataClearExtension;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.Disabled;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

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

            @Test
            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
            void changeEmptyNotFoundTable() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableCommand(2, true)))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("단체 지정이 되어있는 경우 예외가 발생한다.")
            void changeEmptyExistGroup() {
                TableGroup tableGroup = tableGroupRepository.save(
                        new TableGroup(List.of(new OrderTable(2, false),
                                new OrderTable(3, false))));

                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false, tableGroup.getId()));

                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true)))
                        .hasMessage("이미 단체지정이 되어있습니다.");
            }

            @Test
            @Disabled
            @DisplayName("조리중인 경우 예외가 발생한다.")
            void changeEmptyOrderStatusCooking() {
                MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("추천메뉴"));
                Menu menu = menuRepository.save(new Menu("강정치킨", BigDecimal.valueOf(37000), menuGroup.getId()));
                OrderTable orderTable = orderTableRepository.save(new OrderTable(2, true));

                orderRepository.save(new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 2))));

                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableCommand(2, true)))
                        .hasMessage("조리중이거나 식사 상태입니다.");
            }
        }
    }

    @Nested
    @DisplayName("방문한 손님수를 변경할 때")
    class NumberOfGuests {

        @Test
        @DisplayName("정상적으로 변경한다.")
        void changeNumberOfGuests() {
            OrderTableResponse response = tableService.create(new OrderTableCommand(2, false));

            OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(response.getId(),
                    new OrderTableCommand(4, false));

            assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("방문한 손님수가 0 미만인 경우 예외가 발생한다.")
            void changeNumberOfGuestsLessThanZero() {
                OrderTableResponse response = tableService.create(new OrderTableCommand(2, false));

                assertThatThrownBy(
                        () -> tableService.changeNumberOfGuests(response.getId(), new OrderTableCommand(-1, false)))
                        .hasMessage("방문한 손님 수는 0 이상이어야 합니다.");
            }

            @Test
            @DisplayName("주문 테이블이 존재하지 않는 경우 예외가 발생한다.")
            void changeNumberOfGuestsNotFoundGuests() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableCommand(-1, false)))
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("빈 테이블인 경우 예외가 발생한다.")
            void changeNumberOfGuestsOrderTableEmpty() {
                OrderTableResponse response = tableService.create(new OrderTableCommand(2, false));

                OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(response.getId(),
                        new OrderTableCommand(4, true));

                assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
            }
        }
    }
}

