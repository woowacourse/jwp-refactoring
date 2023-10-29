package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.order.domain.OrderLineItemFixture.주문_항목;
import static kitchenpos.table.application.TableServiceTest.OrderTableRequestFixture.주문_테이블_빈_상태로_변경_요청;
import static kitchenpos.table.application.TableServiceTest.OrderTableRequestFixture.주문_테이블_생성_요청;
import static kitchenpos.table.application.TableServiceTest.OrderTableRequestFixture.주문_테이블_손님_수_변경_요청;
import static kitchenpos.table.application.TableServiceTest.OrderTableRequestFixture.주문_테이블_채워진_상태로_변경_요청;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_빈_주문_테이블;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_주문_테이블;
import static kitchenpos.tablegroup.domain.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.vo.MenuSpecification;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableFixture;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableIsEmptyUpdateRequest;
import kitchenpos.table.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "classpath:test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTableCreateRequest request = 주문_테이블_생성_요청();

        // when
        OrderTableResponse orderTable = tableService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(orderTable.getId()).isNotNull();
            softly.assertThat(orderTable).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(OrderTableResponse.from(단체_지정_없는_빈_주문_테이블()));
        });
    }

    @Test
    void 전체_주문을_조회한다() {
        // given
        Long orderTableId = orderTableRepository.save(OrderTableFixture.단체_지정_없는_채워진_주문_테이블()).getId();

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        Assertions.assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(OrderTableResponse.from(단체_지정_주문_테이블(orderTableId, null))));
    }

    @Nested
    class 주문_테이블이_테이블이_비어있는지_여부를_변경할_때 {

        @Test
        void 빈_주문_테이블을_비어있지_않도록_변경한다() {
            // given
            Long emptyOrderTableId = tableService.create(주문_테이블_생성_요청()).getId();

            // when
            OrderTableResponse orderTable = tableService.changeIsEmpty(emptyOrderTableId, 주문_테이블_채워진_상태로_변경_요청());

            // then
            assertThat(orderTable.isEmpty()).isFalse();
        }

        @Test
        void 채워진_주문_테이블을_비어있도록_변경한다() {
            // given
            Long filledOrderTableId = tableService.create(주문_테이블_생성_요청()).getId();

            // when
            OrderTableResponse orderTable = tableService.changeIsEmpty(filledOrderTableId, 주문_테이블_빈_상태로_변경_요청());

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }

        @Test
        void 존재하지_않는_주문_테이블을_변경하려고_하면_예외를_던진다() {
            // given
            Long invalidOrderTableId = Long.MIN_VALUE;

            // expect
            assertThatThrownBy(() -> tableService.changeIsEmpty(invalidOrderTableId, 주문_테이블_채워진_상태로_변경_요청()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_단체_지정되어_있다면_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());

            OrderTable groupedOrderTable = orderTableRepository.save(단체_지정_빈_주문_테이블(tableGroup.getId()));

            // expect
            assertThatThrownBy(() -> tableService.changeIsEmpty(groupedOrderTable.getId(), 주문_테이블_채워진_상태로_변경_요청()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @Sql("/init_for_change_is_empty_test.sql")
        void 주문_테이블에_조리_혹은_식사_중인_주문이_있다면_예외를_던진다(String orderStatus) {
            // given
            Long orderTableId = tableService.create(주문_테이블_생성_요청()).getId();

            Long menuId = 1L;
            orderRepository.save(주문(
                    orderTableId,
                    OrderStatus.valueOf(orderStatus),
                    List.of(주문_항목(menuId, new MenuSpecification("메뉴", BigDecimal.valueOf(10000))))
            ));

            // expect
            assertThatThrownBy(() -> tableService.changeIsEmpty(orderTableId, 주문_테이블_채워진_상태로_변경_요청()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_테이블의_방문한_손님_수를_변경할_때 {

        @ParameterizedTest
        @CsvSource({"1, 0, 0", "0, 1, 1", "1, 2, 2"})
        void 정상적으로_변경한다(int original, int actual, int expected) {
            // given
            OrderTableResponse zeroOrderTable = tableService.create(주문_테이블_생성_요청(original));

            // when
            OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(
                    zeroOrderTable.getId(),
                    주문_테이블_손님_수_변경_요청(actual)
            );

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, -2})
        void 방문한_손님_수가_0미만이면_예외를_던진다(int numberOfGuests) {
            // given
            OrderTableResponse orderTable = tableService.create(주문_테이블_생성_요청());

            // expect
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(orderTable.getId(), 주문_테이블_손님_수_변경_요청(numberOfGuests)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이면_예외를_던진다() {
            // given
            Long invalidOrderTableId = Long.MIN_VALUE;

            // expect
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(invalidOrderTableId, 주문_테이블_손님_수_변경_요청(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있다면_예외를_던진다() {
            // given
            OrderTableResponse emptyOrderTable = tableService.create(주문_테이블_생성_요청());

            // expect
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(emptyOrderTable.getId(), 주문_테이블_손님_수_변경_요청(1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    static class OrderTableRequestFixture {

        public static OrderTableCreateRequest 주문_테이블_생성_요청() {
            return new OrderTableCreateRequest(0, true);
        }

        public static OrderTableCreateRequest 주문_테이블_생성_요청(int numberOfGuests) {
            return new OrderTableCreateRequest(numberOfGuests, false);
        }

        public static OrderTableIsEmptyUpdateRequest 주문_테이블_빈_상태로_변경_요청() {
            return new OrderTableIsEmptyUpdateRequest(true);
        }

        public static OrderTableIsEmptyUpdateRequest 주문_테이블_채워진_상태로_변경_요청() {
            return new OrderTableIsEmptyUpdateRequest(false);
        }

        public static OrderTableNumberOfGuestsUpdateRequest 주문_테이블_손님_수_변경_요청(int numberOfGuests) {
            return new OrderTableNumberOfGuestsUpdateRequest(numberOfGuests);
        }
    }
}
