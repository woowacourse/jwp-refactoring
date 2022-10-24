package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TableService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 2;
            private final boolean isEmpty = false;
            private final OrderTable orderTable = new OrderTable(numberOfGuests, isEmpty);

            @Test
            void 주문_테이블을_추가한다() {
                OrderTable actual = tableService.create(orderTable);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getTableGroupId()).isNull();
                    assertThat(actual.getNumberOfGuests()).isEqualTo(2);
                    assertThat(actual.isEmpty()).isFalse();
                });
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            @Test
            void 주문_테이블_목록을_반환한다() {
                List<OrderTable> orderTables = tableService.list();

                assertThat(orderTables).isNotEmpty();
            }
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, false));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests, true);

            @Test
            void 주문_테이블을_비활성화한다() {
                OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTableToBeChanged);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getNumberOfGuests()).isEqualTo(2);
                    assertThat(actual.isEmpty()).isTrue();
                });
            }
        }

        @Nested
        class 존재하지_않는_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 단체_지정이_되어있는_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            private final OrderTable orderTable = orderTableDao.save(
                    new OrderTable(tableGroup.getId(), numberOfGuests, false));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("단체 지정된 주문 테이블은 비활성화할 수 없습니다.");
            }
        }

        @Nested
        class 조리중인_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, false));
            private final Order order = orderDao.save(
                    new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), List.of()));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
            }
        }

        @Nested
        class 식사중인_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, false));
            private final Order order = orderDao.save(
                    new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), List.of()));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests, true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 1;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests);

            @Test
            void 테이블_손님_수를_변경한다() {
                OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), orderTableToBeChanged);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
                });
            }
        }

        @Nested
        class 손님_수를_0명_미만으로_변경할_경우 {

            private final int numberOfGuests = -1;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("손님 수는 0명 미만일 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_주문_테이블의_손님_수를_변경할_경우 {

            private final int numberOfGuests = 1;
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 비활성화된_주문_테이블의_손님_수를_변경할_경우 {

            private final int numberOfGuests = 1;
            private final OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
            private final OrderTable orderTableToBeChanged = new OrderTable(numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToBeChanged))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("비황성화된 주문 테이블의 손님 수는 변경할 수 없습니다.");
            }
        }
    }
}
