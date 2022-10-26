package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createOrderTable;
import static kitchenpos.fixture.TableFixture.createRequestEmpty;
import static kitchenpos.fixture.TableFixture.createRequestNumberOfGuests;
import static kitchenpos.fixture.TableFixture.빈_테이블_1번;
import static kitchenpos.fixture.TableFixture.사용중인_테이블_1번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = createOrderTable(null, null, 3, true);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        saveAndGetOrderTable();

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("주문 테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable table = orderTableDao.save(빈_테이블_1번);
        OrderTable request = createRequestEmpty(false);

        // when
        OrderTable actual = tableService.changeEmpty(table.getId(), request);

        // then
        assertThat(actual.getId()).isEqualTo(table.getId());
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("changeEmpty 메서드는 tableGroupId이 null이 아니면 예외를 발생시킨다.")
    @Test
    void changeEmpty_tableGroupId_null_throwException() {
        // given
        OrderTable table = orderTableDao.save(빈_테이블_1번);

        TableGroup tableGroup = saveAndGetTableGroup();
        table.setTableGroupId(tableGroup.getId());
        orderTableDao.save(table);

        OrderTable request = createRequestEmpty(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable table = orderTableDao.save(사용중인_테이블_1번);
        OrderTable request = createRequestNumberOfGuests(100);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(table.getId(), request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(100);
    }
}
