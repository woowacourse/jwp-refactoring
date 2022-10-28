package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyUpdateRequest;
import kitchenpos.dto.TableGuestUpdateRequest;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TableServiceTest extends ServiceTest {

    @Test
    void 테이블_생성_메소드는_입력받은_테이블을_저장한다() {
        // given
        TableCreateRequest request = new TableCreateRequest(4, false);

        // when
        TableResponse response = tableService.create(request);

        // then
        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response)
                    .extracting(TableResponse::getNumberOfGuests, TableResponse::isEmpty)
                    .containsExactly(request.getNumberOfGuests(), request.isEmpty());
        });
    }

    @Test
    void 테이블_목록_조회_메소드는_모든_테이블을_조회한다() {
        // given
        OrderTable orderTable1 = 테이블을_저장한다(4);
        OrderTable orderTable2 = 빈_테이블을_저장한다();

        // when
        List<TableResponse> tables = tableService.list();

        // then
        assertThat(tables)
                .extracting(TableResponse::getId, TableResponse::getNumberOfGuests, TableResponse::isEmpty)
                .contains(tuple(orderTable1.getId(), orderTable1.getNumberOfGuests(), orderTable1.isEmpty()),
                        tuple(orderTable2.getId(), orderTable2.getNumberOfGuests(), orderTable2.isEmpty()));
    }

    @Nested
    class changeEmpty_메소드는 extends ServiceTest {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        void 테이블이_비어_있는지_여부를_변경한다(boolean isEmpty) {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);
            TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(isEmpty);

            // when
            tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            OrderTable updatedOrderTable = orderTableDao.findById(savedOrderTable.getId()).get();
            assertThat(updatedOrderTable.isEmpty()).isEqualTo(isEmpty);
        }

        @Test
        void 주문_상태가_계산_완료_상태가_아니라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);
            Order savedOrder = 주문을_저장한다(savedOrderTable);

            Order order = new Order(savedOrder.getId(), savedOrder.getOrderTableId(), OrderStatus.COOKING.name(),
                    savedOrder.getOrderedTime(), Collections.emptyList());
            orderDao.save(order);

            TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹_id를_가지고_있다면_예외가_발생한다() {
            // given
            TableGroup savedTableGroup = tableGroupDao.save(테이블_그룹을_저장한다());
            OrderTable orderTableWithTableGroup = orderTableDao.save(new OrderTable(savedTableGroup.getId(), 0, true));

            TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableWithTableGroup.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외가_발생한다() {
            // given
            TableEmptyUpdateRequest request = new TableEmptyUpdateRequest(true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class changeNumberOfGuests_메소드는 extends ServiceTest {

        @Test
        void 테이블_손님_수를_변경한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            TableGuestUpdateRequest request = new TableGuestUpdateRequest(2);

            // when
            tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

            // then
            OrderTable updatedOrderTable = orderTableDao.findById(savedOrderTable.getId()).get();

            assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(2);
        }

        @Test
        void 존재하지_않는_테이블이라면_예외가_발생한다() {
            // given
            TableGuestUpdateRequest request = new TableGuestUpdateRequest(5);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 비어있는_테이블이라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 빈_테이블을_저장한다();

            TableGuestUpdateRequest request = new TableGuestUpdateRequest(5);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 변경하려는_손님_수가_음수라면_예외가_발생한다() {
            // given
            OrderTable savedOrderTable = 테이블을_저장한다(4);

            TableGuestUpdateRequest request = new TableGuestUpdateRequest(-1);

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
