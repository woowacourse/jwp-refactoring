package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderDao orderDao;


    @DisplayName("테이블을 생성할 수 있다")
    @Test
    void create() {
        // given
        final var expectedNumberOfGuests = 0;
        final var expectedEmpty = true;
        final var orderTableRequest = new OrderTableCreateRequest(expectedNumberOfGuests, expectedEmpty);

        // when
        final var actual = tableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests),
                () -> assertThat(actual.isEmpty()).isEqualTo(expectedEmpty)
        );
    }

    @DisplayName("테이블 생성 시, numberOfGuests가 0 보다 작으면 예외가 발생한다")
    @Test
    void create_should_fail_when_numberOfGuest_is_less_than_zero() {
        // given
        final var orderTableRequest = new OrderTableCreateRequest(-1, true);

        // when & then
        assertThrows(
                IllegalArgumentException.class,
                () -> tableService.create(orderTableRequest)
        );
    }

    @DisplayName("전체 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        tableService.create(new OrderTableCreateRequest(0, true));
        tableService.create(new OrderTableCreateRequest(3, false));
        tableService.create(new OrderTableCreateRequest(5, true));

        // when
        final var actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).extracting("id").containsExactly(1L, 2L, 3L),
                () -> assertThat(actual).extracting("tableGroupId").containsExactly(null, null, null),
                () -> assertThat(actual).extracting("numberOfGuests").containsExactly(0, 3, 5),
                () -> assertThat(actual).extracting("empty").containsExactly(true, false, true)
        );
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class ChangeEmpty {
        @DisplayName("테이블의 주문 가능 여부를 변경할 수 있다")
        @Test
        void changes_table_order_availability() {
            // given
            final var table = tableService.create(new OrderTableCreateRequest(0, true));

            // when
            final var emptyChangeRequest = new OrderTableChangeEmptyRequest(false);
            final var emptyChangedTable = tableService.changeEmpty(table.getId(), emptyChangeRequest);

            // then
            assertAll(
                    () -> assertThat(table.isEmpty()).isTrue(),
                    () -> assertThat(emptyChangedTable.isEmpty()).isFalse(),
                    () -> assertThat(emptyChangedTable.getId()).isEqualTo(table.getId())
            );
        }

        @DisplayName("유효하지 않은 테이블 아이디가 전달되면 예외가 발생한다")
        @Test
        void should_fail_on_invalid_tableId() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeEmpty(-1L, new OrderTableChangeEmptyRequest(true))
            );
        }

        @DisplayName("대상 테이블에 그룹 아이디가 존재하면(그룹에 속해 있는 테이블) 예외이다")
        @Test
        void should_fail_when_target_table_is_assigned_to_a_tableGroup() {
            // given
            final var table1 = tableService.create(new OrderTableCreateRequest(0, true));
            final var table2 = tableService.create(new OrderTableCreateRequest(0, true));
            final var tableGroupCreateRequest = new TableGroupCreateRequest(List.of(table1.getId(), table2.getId()));
            final var groupedTables = tableGroupService.create(tableGroupCreateRequest).getOrderTables();

            // when & then
            assertAll(
                    () -> assertThat(groupedTables)
                            .extracting("tableGroupId")
                            .containsExactly(1L, 1L),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> tableService.changeEmpty(table1.getId(),
                                    new OrderTableChangeEmptyRequest(table1.isEmpty()))
                    ),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> tableService.changeEmpty(table2.getId(),
                                    new OrderTableChangeEmptyRequest(table2.isEmpty()))
                    )
            );
        }

        @DisplayName("대상 테이블에 조리시작(COOKING) 상태인 주문이 존재할 경우 예외가 발생한다")
        @Test
        void should_fail_when_target_table_has_a_cooking_status_order() {
            // given
            final var groupedTable = tableService.create(new OrderTableCreateRequest(0, true));
            orderDao.save(new Order(groupedTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), null));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeEmpty(groupedTable.getId(),
                            new OrderTableChangeEmptyRequest(groupedTable.isEmpty()))
            );
        }

        @DisplayName("대상 테이블에 식사중(MEAL) 상태인 주문이 존재할 경우 예외가 발생한다")
        @Test
        void should_fail_when_target_table_has_a_meal_status_order() {
            // given
            final var groupedTable = tableService.create(new OrderTableCreateRequest(0, true));
            orderDao.save(new Order(groupedTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null));

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeEmpty(groupedTable.getId(),
                            new OrderTableChangeEmptyRequest(groupedTable.isEmpty()))
            );
        }
    }

    @DisplayName("changeNumberOfGuests 메서드는")
    @Nested
    class ChangeNumberOfGuests {
        @DisplayName("주문 가능한 테이블의 고객 인원 수를 변경할 수 있다")
        @Test
        void change_number_of_guests() {
            // given
            final var table = tableService.create(new OrderTableCreateRequest(0, false));
            final var beforeChange = table.getNumberOfGuests();

            // when
            final var changeRequest = new OrderTableChangeNumberOfGuestsRequest(10);
            final var changed = tableService.changeNumberOfGuests(table.getId(), changeRequest);
            final var afterChange = changed.getNumberOfGuests();

            // then
            assertAll(
                    () -> assertThat(beforeChange).isEqualTo(0),
                    () -> assertThat(afterChange).isEqualTo(10),
                    () -> assertThat(changed.getId()).isEqualTo(table.getId()),
                    () -> assertThat(changed.getTableGroupId()).isEqualTo(table.getTableGroupId())
            );
        }

        @DisplayName("변경하려는 고객 인원수가 0 보다 작으면 예외이다")
        @Test
        void should_fail_when_request_number_is_less_then_zero() {
            // given
            final var table = tableService.create(new OrderTableCreateRequest(0, false));
            final var changeRequest = new OrderTableChangeNumberOfGuestsRequest(-1);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), changeRequest)
            );
        }

        @DisplayName("유효하지 않은 테이블 아이디일 경우 예외이다")
        @Test
        void should_fail_on_invalid_table_id() {
            // given
            final var changeRequest = new OrderTableChangeNumberOfGuestsRequest(0);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(-1L, changeRequest)
            );
        }

        @DisplayName("주문 불가 상태(empty=true) 테이블의 고객 인원수를 변경하려 하면 예외이다")
        @Test
        void should_fail_when_target_table_is_empty() {
            // given
            final var table = tableService.create(new OrderTableCreateRequest(0, true));
            final var changeRequest = new OrderTableChangeNumberOfGuestsRequest(10);

            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> tableService.changeNumberOfGuests(table.getId(), changeRequest)
            );
        }
    }
}
