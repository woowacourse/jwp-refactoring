package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixtures.TestFixtures.단체_지정_생성;
import static kitchenpos.fixtures.TestFixtures.주문_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static kitchenpos.fixtures.TestFixtures.주문_항목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("MenuGroupService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_테이블이_입력되면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);

            @Test
            void 해당_테이블을_반환한다() {
                final OrderTable savedOrderTable = tableService.create(orderTable);

                assertThat(savedOrderTable.getId()).isNotNull();
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 요청되면 extends ServiceTest {

            @Test
            void 모든_주문_테이블을_반환한다() {
                final List<OrderTable> orderTables = tableService.list();

                assertThat(orderTables).isEmpty();
            }
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 주문_테이블이_완료_상태면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);
            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final Order order = 주문_생성(1L, COMPLETION.name(), LocalDateTime.now(), List.of(orderLineItem));
            private final OrderTable orderTableToUpdateTo = 주문_테이블_생성(null, 0, true);

            @BeforeEach
            void setUp() {
                orderTableDao.save(orderTable);
                orderDao.save(order);
            }

            @Test
            void 빈_테이블로_변경한다() {
                final OrderTable changedOrderTable = tableService.changeEmpty(1L, orderTableToUpdateTo);

                assertThat(changedOrderTable.isEmpty()).isTrue();
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않는다면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 단체_지정이_null이면 extends ServiceTest {

            private final TableGroup tableGroup = 단체_지정_생성(LocalDateTime.now(), null);
            private final OrderTable orderTable = 주문_테이블_생성(1L, 5, false);

            @BeforeEach
            void setUp() {
                tableGroupDao.save(tableGroup);
                orderTableDao.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 이미_주문이_존재하면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);
            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 1L, 5);
            private final Order order = 주문_생성(1L, COOKING.name(), LocalDateTime.now(), List.of(orderLineItem));

            @BeforeEach
            void setUp() {
                orderTableDao.save(orderTable);
                orderDao.save(order);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 손님_수가_음수면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, -1, false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않으면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_비어있으면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 0, true);
            private OrderTable savedOrderTable;

            @BeforeEach
            void setUp() {
                savedOrderTable = orderTableDao.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
