package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixtures.TestFixtures.단체_지정_생성;
import static kitchenpos.fixtures.TestFixtures.주문_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_Empty_변경_요청;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_생성_요청;
import static kitchenpos.fixtures.TestFixtures.주문_테이블_손님_수_변경_요청;
import static kitchenpos.fixtures.TestFixtures.주문_항목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableResponse;
import kitchenpos.ui.dto.TableUpdateEmptyRequest;
import kitchenpos.ui.dto.TableUpdateGuestNumberRequest;
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

            private final TableCreateRequest request = 주문_테이블_생성_요청(5, true);

            @Test
            void 해당_테이블을_반환한다() {
                final TableResponse response = tableService.create(request);

                assertThat(response.getId()).isNotNull();
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 요청되면 extends ServiceTest {

            @Test
            void 모든_주문_테이블을_반환한다() {
                final List<TableResponse> responses = tableService.list();

                assertThat(responses).isEmpty();
            }
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 주문_테이블이_완료_상태면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, false);
            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 5);
            private final Order order = 주문_생성(orderTable, COMPLETION, LocalDateTime.now(), List.of(orderLineItem));
            private final TableUpdateEmptyRequest updateEmptyRequest = 주문_테이블_Empty_변경_요청(true);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
                orderRepository.save(order);
            }

            @Test
            void 빈_테이블로_변경한다() {
                final TableResponse tableResponse = tableService.changeEmpty(1L, updateEmptyRequest);

                assertThat(tableResponse.isEmpty()).isTrue();
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않는다면 extends ServiceTest {

            private final TableUpdateEmptyRequest updateEmptyRequest = 주문_테이블_Empty_변경_요청(false);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, updateEmptyRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 단체_지정이_null이_아니면 extends ServiceTest {

            private final TableGroup tableGroup = 단체_지정_생성(LocalDateTime.now(), null);
            private final OrderTable orderTable = 주문_테이블_생성(tableGroup, 5, false);
            private final TableUpdateEmptyRequest updateEmptyRequest = 주문_테이블_Empty_변경_요청(false);

            @BeforeEach
            void setUp() {
                tableGroupRepository.save(tableGroup);
                orderTableRepository.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, updateEmptyRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 이미_주문이_존재하면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 5, true);
            private final OrderLineItem orderLineItem = 주문_항목_생성(1L, 5);
            private final Order order = 주문_생성(orderTable, COOKING, LocalDateTime.now(), List.of(orderLineItem));
            private final TableUpdateEmptyRequest updateEmptyRequest = 주문_테이블_Empty_변경_요청(false);

            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
                orderRepository.save(order);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(1L, updateEmptyRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 손님_수가_음수면 extends ServiceTest {

            private final TableUpdateGuestNumberRequest updateGuestNumberRequest = 주문_테이블_손님_수_변경_요청(-1);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않으면 extends ServiceTest {

            private final TableUpdateGuestNumberRequest updateGuestNumberRequest = 주문_테이블_손님_수_변경_요청(5);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        class 주문_테이블이_비어있으면 extends ServiceTest {

            private final OrderTable orderTable = 주문_테이블_생성(null, 0, true);
            private final TableUpdateGuestNumberRequest updateGuestNumberRequest = 주문_테이블_손님_수_변경_요청(5);


            @BeforeEach
            void setUp() {
                orderTableRepository.save(orderTable);
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
