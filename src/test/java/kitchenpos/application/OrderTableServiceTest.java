package kitchenpos.application;

import kitchenpos.EntityFactory;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityFactory entityFactory;

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다")
    void create() {
        //given
        final OrderTable request = new OrderTable();
        request.setNumberOfGuests(5);

        //when
        final OrderTable orderTable = orderTableService.create(request);

        //then
        assertSoftly(softAssertions -> {
            assertThat(orderTable.getId()).isNotNull();
            assertThat(orderTable.getTableGroupId()).isNull();
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("주문 테이블 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> orderTableService.list());
    }

    @Nested
    @DisplayName("주문 테이블의 빈 테이블 여부 변경 테스트")
    class ChangeEmptyTest {

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다")
        void changeEmpty() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when
            final OrderTable changedOrderTable = orderTableService.changeEmpty(orderTable.getId(), request);

            //then
            assertThat(changedOrderTable.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeEmpty_fail() {
            //given
            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 단체 지정이 존재하면 예외가 발생한다")
        void changeEmpty_fail2() {
            //given
            final TableGroup tableGroup = entityFactory.saveTableGroup();
            final OrderTable orderTable = entityFactory.saveOrderTableWithTableGroup(tableGroup);

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 때 주문 상태가 COOKING 또는 MEAL이면 예외가 발생한다")
        void changeEmpty_fail3(final String status) {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();

            final OrderTable request = new OrderTable();
            request.setEmpty(true);

            final Order order = entityFactory.saveOrder(orderTable);
            final Order requestToChangeStatus = new Order();
            requestToChangeStatus.setOrderStatus(status);
            orderService.changeOrderStatus(order.getId(), requestToChangeStatus);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 테이블의 방문한 손님 수 변경 테스트")
    class ChangeNumberOfGuestsTest {

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다")
        void changeNumberOfGuests() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTableWithNotEmpty();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when
            final OrderTable changedOrderTable = orderTableService.changeNumberOfGuests(orderTable.getId(), request);

            //then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 음수로 변경하면 예외가 발생한다")
        void changeNumberOfGuests_fail() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(-1);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 존재하지 않으면 예외가 발생한다")
        void changeNumberOfGuests_fail2() {
            //given
            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(0L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블의 방문한 손님 수를 변경할 때 주문 테이블이 비어 있으면 예외가 발생한다")
        void changeNumberOfGuests_fail3() {
            //given
            final OrderTable orderTable = entityFactory.saveOrderTable();

            final OrderTable request = new OrderTable();
            request.setNumberOfGuests(10);

            //when, then
            assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
