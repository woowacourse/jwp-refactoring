package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @MockBean
    private OrderDao orderDao;

    @DisplayName("테이블을 저장한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId")
                .isEqualTo(orderTable);
    }

    @DisplayName("테이블을 전체 조회한다.")
    @Test
    void findAll() {
        // given
        orderTableDao.save(new OrderTable(4, false));

        // when, then
        assertThat(tableService.findAll()).hasSize(1);
    }

    @DisplayName("테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        final OrderTable orderTableForUpdate = new OrderTable(true);
        final OrderTable updatedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTableForUpdate);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.isEmpty()).isTrue(),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("empty")
                        .isEqualTo(savedOrderTable)
        );

    }

    @DisplayName("테이블이 존재하지 않으면 사용 여부를 변경할 시 예외를 반환한다.")
    @Test
    void changeEmpty_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(invalidTableId, new OrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블입니다.");
    }

    @DisplayName("테이블이 그룹핑되어 있으면 사용여부를 변경할 수 없다.")
    @Test
    void changeEmpty_throwException_ifTableGrouping() {
        // given
        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = new OrderTable(savedTableGroup.getId(), 4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);
        savedOrderTable.setTableGroupId(1L);
        orderTableDao.save(savedOrderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 테이블입니다.");
    }

    @DisplayName("테이블에 주문이 들어가거나 식사 상태이면 사용여부를 변경할 수 없다.")
    @Test
    void changeEmpty_throwException_ifOrderAlreadyOngoing() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);
        final OrderTable orderTable = new OrderTable(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable(true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 주문이 들어간 테이블입니다.");
    }

    @DisplayName("테이블 인원 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        final OrderTable orderTableForUpdate = new OrderTable(5);
        final OrderTable updatedOrderTable = tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.getNumberOfGuests())
                        .isEqualTo(orderTableForUpdate.getNumberOfGuests()),
                () -> assertThat(updatedOrderTable).usingRecursiveComparison()
                        .ignoringFields("numberOfGuests")
                        .isEqualTo(savedOrderTable)
        );
    }

    @DisplayName("테이블 인원 수는 음수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throwException_ifNumberOfGuestsIsNegative() {
        // given
        final OrderTable orderTable = new OrderTable(4, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when, then
        final OrderTable orderTableForUpdate = new OrderTable(-1);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 인원은 음수가 될 수 없습니다.");
    }

    @DisplayName("테이블이 존재하지 않을 경우 테이블 인원 변경은 불가하다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableNotExist() {
        // given
        final Long invalidTableId = 999L;

        // when, then
        final OrderTable orderTableForUpdate = new OrderTable(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(invalidTableId, orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("사용 중이지 않은 테이블의 인원 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throwException_ifTableEmpty() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // when, then
        final OrderTable orderTableForUpdate = new OrderTable(5);
        assertThatThrownBy(() -> tableService
                .changeNumberOfGuests(savedOrderTable.getId(), orderTableForUpdate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 중이지 않은 테이블의 인원 수를 변경할 수 없습니다.");
    }
}
