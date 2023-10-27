package kitchenpos.application;

import com.sun.tools.javac.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.ordertable.ChangeEmptyRequest;
import kitchenpos.dto.ordertable.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable savedEmptyOrderTable;

    private OrderTable savedFullOrderTable;

    @BeforeEach
    void setup() {
        OrderTableRequest emptyOrderTableRequest = new OrderTableRequest(0, true);
        savedEmptyOrderTable = tableService.create(emptyOrderTableRequest);

        OrderTableRequest fullOrderTableRequest = new OrderTableRequest(4, false);
        savedFullOrderTable = tableService.create(fullOrderTableRequest);
    }

    @Test
    @DisplayName("테이블 등록에 성공한다.")
    void succeedInRegisteringTable() {
        // given
        int numberOfGuest = 4;
        boolean tableStatus = false;

        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuest, tableStatus);

        // when
        OrderTable savedOrdertable = tableService.create(orderTableRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrdertable.getId()).isNotNull();
            softly.assertThat(savedOrdertable.getNumberOfGuests()).isEqualTo(numberOfGuest);
            softly.assertThat(savedOrdertable.isEmpty()).isFalse();
        });
    }

    @Test
    @DisplayName("전체 테이블 조회에 성공한다.")
    void succeedInSearchingTableList() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

        // when
        tableService.create(orderTableRequest);

        // then
        assertThat(tableService.list()).hasSize(3);
    }

    @Test
    @DisplayName("테이블의 손님 유무 상태를 변경에 성공한다.")
    void succeedInChangingTableStatus() {
        // give
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(false);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(savedEmptyOrderTable.getId(), changeEmptyRequest);

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("등록된 테이블이 존재하지 않을 경우 테이블 상태 변경 시 예외가 발생한다.")
    void failToChangeTableStatusWithUnRegisteredTable() {
        // given
        Long unSavedOrderTableId = 1000L;

        // when
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(false);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(unSavedOrderTableId, changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 그룹으로 지정된 테이블의 상태를 변경할 경우 예외가 발생한다.")
    void failToChangeTableStatusAlreadyGrouped() {
        // given
        OrderTable emptyTable2 = new OrderTable(0, true);
        OrderTable saveEmptyTable2 = orderTableRepository.save(emptyTable2);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(savedEmptyOrderTable, saveEmptyTable2));
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        OrderTable orderTable = new OrderTable(4, false);
        orderTable.setTableGroupId(savedTableGroup.getId());
        OrderTable saveTable = orderTableRepository.save(orderTable);

        // when
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(saveTable.getId(), changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹으로 지정된 테이블의 상태를 변경할 수 없습니다.");
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 조리 중이거나 식사 중인 테이블의 상태를 변경할 경우 예외가 발생한다.")
    void failToChangeTableStatusWithOrderStatus(OrderStatus orderStatus) {
        // given
        Order order = new Order(savedFullOrderTable, orderStatus.name(), LocalDateTime.now());
        orderRepository.save(order);

        // when
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedFullOrderTable.getId(), changeEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 주문의 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("손님수 변경에 성공한다.")
    void succeedInChangingNumberOfGuests() {
        // given
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(2);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedFullOrderTable.getId(), changeNumberOfGuestsRequest);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("변경하려는 손님 수가 0미만일 경우 예외가 발생한다.")
    void failToChangeNumberOfGuestWithWrongNumber() {
        // given & when
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-4);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedFullOrderTable.getId(), changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사전에 등록된 테이블이 없는 상태에서 테이블 손님 수를 변경할 경우 예외가 발생한다.")
    void failToChangeNumberOfGuestWithNonExistTable() {
        // given
        Long unsavedOrderTableId = 10000L;

        // when
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(2);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(unsavedOrderTableId, changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 빈 상태일 때 손님 수를 변경하면 예외가 발생한다.")
    void failToChangeNumberOfGuestWithNotEmptyTable() {
        // given & when
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(2);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedEmptyOrderTable.getId(), changeNumberOfGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈테이블은 인원수를 변경할 수 없습니다.");
    }
}
