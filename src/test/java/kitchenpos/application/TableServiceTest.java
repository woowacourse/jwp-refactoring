package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static kitchenpos.support.OrderTableFixture.주문테이블_요청;
import static kitchenpos.support.TableGroupFixture.빈_테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.emptyList;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest {

    @Nested
    class create_메서드는 extends IntegrationServiceTest {

        @Test
        void 주문테이블을_저장하고_반환한다() {

            OrderTable actual = tableService.create(주문테이블_요청);

            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class list_메서드는 extends IntegrationServiceTest {

        @BeforeEach
        void setUp() {
            tableRepository.save(new OrderTable(1, true, null));
            tableRepository.save(new OrderTable(2, true, null));
            tableRepository.save(new OrderTable(3, true, null));
            tableRepository.save(new OrderTable(4, true, null));
        }

        @Test
        void 주문테이블목록을_반환한다() {

            List<OrderTable> actual = tableService.list();

            assertThat(actual).hasSize(4);
        }
    }

    @Nested
    class changeEmpty_메서드는 extends IntegrationServiceTest {

        @Nested
        class 존재하지않는_주문테이블_ID가_입력된_경우 extends IntegrationServiceTest {

            private final long NOT_EXISTS_ID = -1L;

            @Test
            void 예외가_발생한다() {

                assert_throws_changeEmpty("존재하지 않는 주문 테이블 ID입니다.", NOT_EXISTS_ID, 주문테이블_요청);
            }
        }

        @Nested
        class 단체지정된_주문테이블이_입력된_경우 extends IntegrationServiceTest {

            private Long savedOrderTableId;

            @BeforeEach
            void setUp() {
                final TableGroup 테이블_그룹 = tableGroupRepository.save(빈_테이블_그룹);
                final OrderTable savedOrderTable = tableRepository.save(new OrderTable(0, false, 테이블_그룹));
                savedOrderTableId = savedOrderTable.getId();
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_changeEmpty("주문 테이블이 단체 지정되었을 경우 상태를 변경할 수 없습니다.",
                        savedOrderTableId,
                        주문테이블_요청);
            }
        }

        @Nested
        class 주문테이블에_조리상태의_주문이_있는_경우 extends IntegrationServiceTest {

            private Long savedTableId;

            @BeforeEach
            void setUp() {
                final OrderTable 주문_테이블 = 주문테이블_저장();
                savedTableId = 주문_테이블.getId();
                orderRepository.save(new Order(null, COOKING, now(), 주문_테이블, emptyList()));
            }

            @Test
            void 예외가_발생한다() {

                assert_throws_changeEmpty("주문 테이블이 계산 완료되었을 경우에만 상태를 변경할 수 있습니다.",
                        savedTableId,
                        주문테이블_요청);
            }
        }

        @Nested
        class 주문테이블에_식사상태의_주문이_있는_경우 extends IntegrationServiceTest {

            private Long savedOrderTableId;

            @BeforeEach
            void setUp() {
                OrderTable savedOrderTable = 주문테이블_저장();
                orderRepository.save(new Order(null, MEAL, now(), savedOrderTable, emptyList()));
                savedOrderTableId = savedOrderTable.getId();
            }

            @Test
            void 예외가_발생한다() {
                assert_throws_changeEmpty("주문 테이블이 계산 완료되었을 경우에만 상태를 변경할 수 있습니다.",
                        savedOrderTableId,
                        주문테이블_요청
                );
            }
        }

        @Nested
        class 정상적으로_변경_가능한_경우 extends IntegrationServiceTest {

            private Long savedOrderTableId = 1L;

            @BeforeEach
            void setUp() {
                final OrderTable savedOrderTable = tableRepository.save(new OrderTable(4, true));
                savedOrderTableId = savedOrderTable.getId();
            }

            @Test
            void 주문테이블의_상태를_변경하고_변경된_상태의_주문테이블을_반환한다() {

                OrderTable actual = tableService.changeEmpty(savedOrderTableId, new OrderTableRequest(4, false));

                assertThat(actual.isEmpty()).isFalse();
            }
        }

        private void assert_throws_changeEmpty(String message,
                                               Long orderTableId,
                                               OrderTableRequest changeOrderTableRequest) {

            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeOrderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 변경할_손님의_수가_음수인_경우 extends IntegrationServiceTest {

            private static final int NEGATIVE_NUMBER_OF_GUESTS = -1;
            private final Long orderTableId = 1L;
            private final OrderTableRequest changeOrderTableRequest =
                    new OrderTableRequest(NEGATIVE_NUMBER_OF_GUESTS, false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("손님의 수는 음수가 될 수 없습니다.");
            }
        }

        @Nested
        class 존재하지않는_주문테이블을_입력한_경우 extends IntegrationServiceTest {

            private final Long NOT_EXISTS_ORDER_TABLE_ID = -1L;
            private final OrderTableRequest changeOrderTable = new OrderTableRequest(2, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(NOT_EXISTS_ORDER_TABLE_ID, changeOrderTable))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("변경 요청한 주문테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 저장되어있는_주문테이블이_비어있는_경우 extends IntegrationServiceTest {

            private Long orderTableId = 1L;
            private final OrderTableRequest changeOrderTableRequest = new OrderTableRequest(4, true);

            @BeforeEach
            void setUp() {
                orderTableId = tableRepository.save(new OrderTable(0, true)).getId();
            }

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 비어있는 경우 손님 수를 변경할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_경우 extends IntegrationServiceTest {

            private static final int CHANGE_NUMBER_OF_GUESTS = 4;

            private Long orderTableId;

            @BeforeEach
            void setUp() {
                final OrderTable orderTable = tableRepository.save(new OrderTable(4, false, null));
                orderTableId = orderTable.getId();
            }

            @Test
            void 손님의_수를_변경하고_주문테이블을_반환한다() {

                OrderTable actual = tableService.changeNumberOfGuests(orderTableId,
                        new OrderTableRequest(CHANGE_NUMBER_OF_GUESTS, false));

                assertThat(actual.getNumberOfGuests()).isEqualTo(CHANGE_NUMBER_OF_GUESTS);
            }
        }
    }
}
