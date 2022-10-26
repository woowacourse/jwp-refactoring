package kitchenpos.application;

import static kitchenpos.fixture.Fixture.OrderTableId.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        // when
        OrderTableResponse actual = tableService.create(orderTableRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(3),
            () -> assertThat(actual.getEmpty()).isEqualTo(true),
            () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).hasSize(8);
    }

    @DisplayName("테이블 상태를 변경을")
    @Nested
    class ChangeEmptyTest {

        @DisplayName("비어 있지 않음으로 할 수 있다.")
        @Test
        void changeEmpty() {
            // when
            tableService.changeEmpty(첫번째_테이블, false);

            // then
            assertThat(orderTableRepository.findById(첫번째_테이블))
                .map(OrderTable::isEmpty)
                .get()
                .isEqualTo(false);
        }

        @DisplayName("없는 테이블로 할 수 없다.")
        @Test
        void notExistTable() {
            // when then
            assertThatThrownBy(() -> tableService.changeEmpty(없는_테이블, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
        }

        @DisplayName("그룹으로 지정되어 있으면 할 수 없다.")
        @Test
        void existGroupId() {
            // given
            tableGroupService.create(new TableGroupRequest(List.of(첫번째_테이블, 두번째_테이블)));

            // when then
            assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체로 지정된 테이블은 상태를 변경할 수 없습니다.");
        }

        @DisplayName("주문이 완료되지 않았으면 할 수 없다.")
        @Test
        void notCompletionOrder() {
            // given
            orderDao.save(new Order(첫번째_테이블, "COOKING", LocalDateTime.now()));

            // when then
            assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 완료되지 않아 상태를 변경할 수 없습니다.");
        }
    }

    @DisplayName("테이블 손님 수 변경을")
    @Nested
    class ChangeNumberOfGuestsTest {

        @BeforeEach
        void changeEmpty() {
            tableService.changeEmpty(첫번째_테이블, false);
        }

        @DisplayName("할 수 있다.")
        @Test
        void changeNumberOfGuests() {
            // when
            tableService.changeNumberOfGuests(첫번째_테이블, 5);

            // then
            assertThat(orderTableRepository.findById(첫번째_테이블))
                .map(OrderTable::getNumberOfGuests)
                .get()
                .isEqualTo(5);
        }

        @DisplayName("0명 미만으로 할 수 없다.")
        @Test
        void minusGuests() {
            // when then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(첫번째_테이블, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1명 이상으로 변경할 수 있습니다.");
        }

        @DisplayName("없는 테이블에는 할 수 없다.")
        @Test
        void notExistTable() {
            // when then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-없는_테이블, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
        }

        @DisplayName("비어 있는 테이블에는 할 수 없다.")
        @Test
        void emptyTrue() {
            // when then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(두번째_테이블, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 손님 수 변경을 할 수 없습니다.");
        }
    }
}
