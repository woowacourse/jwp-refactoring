package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.빈_테이블_1번;
import static kitchenpos.fixture.TableFixture.사용중인_테이블_1번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.ordertable.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() {
        // given
        final OrderTableCreateRequest request = createOrderTableCreateRequest(3, true);

        // when
        final OrderTableResponse actual = tableService.create(request);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        saveAndGetOrderTable();

        // when
        final List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문 테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable table = orderTableDao.save(빈_테이블_1번);
        final OrderTableUpdateEmptyRequest request = createOrderTableUpdateRequest(false);

        // when
        final OrderTableResponse actual = tableService.changeEmpty(table.getId(), request);

        // then
        assertThat(actual.getId()).isEqualTo(table.getId());
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("changeEmpty 메서드는 tableGroupId이 null이 아니면 예외를 발생시킨다.")
    @Test
    void changeEmpty_tableGroupId_null_throwException() {
        // given
        final OrderTable table = orderTableDao.save(빈_테이블_1번);

        final TableGroup tableGroup = saveAndGetTableGroup();
        table.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table);

        final OrderTableUpdateEmptyRequest request = createOrderTableUpdateRequest(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable table = orderTableDao.save(사용중인_테이블_1번);
        final OrderTableUpdateNumberOfGuestsRequest request =
            createOrderTableUpdateRequest(100);

        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(100);
    }
}
