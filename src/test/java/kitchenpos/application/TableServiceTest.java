package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.emptyList;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        private final OrderTable orderTable = new OrderTable(1, true);

        @Test
        void 주문테이블을_저장하고_반환한다() {

            OrderTable actual = tableService.create(orderTable);

            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class list_메서드는 {

        @Test
        void 주문테이블목록을_반환한다() {

            List<OrderTable> actual = tableService.list();

            assertThat(actual).hasSize(8);
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 존재하지않는_주문테이블_id가_입력된_경우 {

            private final long NOT_EXISTS_ID = -1L;
            private final OrderTable orderTable = new OrderTable(0, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(NOT_EXISTS_ID, orderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 주문 테이블 ID입니다.");
            }
        }

        @Nested
        class 단체지정된_주문테이블이_입력된_경우 extends IntegrationServiceTest {

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(0, false);

            @BeforeEach
            void setUp() {
                Long savedTableGroupId = tableGroupDao.save(new TableGroup(now(), emptyList())).getId();
                orderTableDao.save(new OrderTable(orderTableId, savedTableGroupId, 0, false));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 단체 지정되었을 경우 상태를 변경할 수 없습니다.");
            }
        }

        @Nested
        class 주문테이블에_조리상태의_주문이_있는_경우 extends IntegrationServiceTest {

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(4, false);

            @BeforeEach
            void setUp() {
                orderDao.save(new Order(orderTableId, COOKING.name(), now(), emptyList()));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 계산 완료되었을 경우에만 상태를 변경할 수 있습니다.");
            }
        }

        @Nested
        class 주문테이블에_식사상태의_주문이_있는_경우 extends IntegrationServiceTest {

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(4, false);

            @BeforeEach
            void setUp() {
                orderDao.save(new Order(orderTableId, MEAL.name(), now(), new ArrayList<>()));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 계산 완료되었을 경우에만 상태를 변경할 수 있습니다.");
            }
        }

        @Nested
        class 정상적으로_변경_가능한_경우 {

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(4, false);

            @Test
            void 주문테이블의_상태를_변경하고_변경된_상태의_주문테이블을_반환한다() {
                OrderTable actual = tableService.changeEmpty(orderTableId, changeOrderTable);

                assertThat(actual.isEmpty()).isFalse();
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 변경할_손님의_수가_음수인_경우 {

            private static final int NEGATIVE_NUMBER_OF_GUESTS = -1;
            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(NEGATIVE_NUMBER_OF_GUESTS, false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("손님의 수는 음수가 될 수 없습니다.");
            }
        }

        @Nested
        class 존재하지않는_주문테이블을_입력한_경우 {

            private final Long NOT_EXISTS_ORDER_TABLE_ID = -1L;
            private final OrderTable changeOrderTable = new OrderTable(2, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(NOT_EXISTS_ORDER_TABLE_ID, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("변경 요청한 주문테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 주문테이블이_비어있는_경우 {

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(0, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 비어있는 경우 손님 수를 변경할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_경우 extends IntegrationServiceTest {

            private static final int CHANGE_NUMBER_OF_GUESTS = 4;

            private final Long orderTableId = 1L;
            private final OrderTable changeOrderTable = new OrderTable(CHANGE_NUMBER_OF_GUESTS, false);

            @BeforeEach
            void setUp() {
                orderTableDao.save(new OrderTable(1L, null, 0, false));
            }

            @Test
            void 손님의_수를_변경하고_주문테이블을_반환한다() {

                OrderTable actual = tableService.changeNumberOfGuests(orderTableId, changeOrderTable);

                assertThat(actual.getNumberOfGuests()).isEqualTo(CHANGE_NUMBER_OF_GUESTS);
            }
        }
    }
}