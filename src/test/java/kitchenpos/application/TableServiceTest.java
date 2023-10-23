package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Nested
    class 테이블_등록 {

        @Test
        void 테이블을_등록할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var request = OrderTableFixture.테이블요청_생성(orderTable);

            // when
            final var actual = tableService.create(request);

            // then
            final var expected = OrderTableResponse.toResponse(orderTable);
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }
    }

    @Nested
    class 테이블_목록_조회 {

        @Test
        void 테이블_목록을_조회할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            // when
            final var actual = tableService.list();

            // then
            final var expected = Collections.singletonList(OrderTableResponse.toResponse(savedOrderTable));
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class 테이블_상태_변경 {

        @Test
        void 주문_테이블을_빈_테이블로_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var request = OrderTableFixture.테이블요청_empty_수정_생성(true);

            // when
            final var actual = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_INVALID();
            final var request = OrderTableFixture.테이블요청_empty_수정_생성(true);

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정되어_있는_테이블이면_예외가_발생한다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var tableGroup = TableGroupFixture.단체지정_여러_테이블(savedOrderTables);
            단일_단체지정_저장(tableGroup);

            orderTable1.changeEmpty(false);

            final var request = OrderTableFixture.테이블요청_empty_수정_생성(true);

            // when & then
            final var id = orderTable1.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_조리_또는_식사면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var savedMenuGroup = 단일_메뉴그룹_저장(menuGroup);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            복수_상품_저장(product1, product2);

            final var menuProduct1 = MenuProductFixture.메뉴상품_생성(product1, 2L);
            final var menuProduct2 = MenuProductFixture.메뉴상품_생성(product2, 1L);
            final var menu = MenuFixture.메뉴_망고치킨_17000원(savedMenuGroup, menuProduct1, menuProduct2);
            final var savedMenu = 단일_메뉴_저장(menu);

            final var orderLineItem = OrderLineItemFixture.주문항목_망고치킨_2개(savedMenu);
            final var order = 단일_주문_저장(savedOrderTable, orderLineItem);
            order.changeOrderStatus(MEAL);

            final var request = OrderTableFixture.테이블요청_empty_수정_생성(true);

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeEmpty(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문한_손님_수_변경 {

        @Test
        void 주문_테이블의_방문한_손님_수를_변경할_수_있다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var changedNumberOfGuests = 10;
            final var request = OrderTableFixture.테이블요청_손님수_수정_생성(changedNumberOfGuests);

            // when
            final var actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

            // then
            assertThat(actual.getNumberOfGuests()).usingRecursiveComparison()
                    .isEqualTo(changedNumberOfGuests);
        }

        @Test
        void 방문한_손님_수가_0보다_작으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_N명(1);
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var changedNumberOfGuests = -1;
            final var request = OrderTableFixture.테이블요청_손님수_수정_생성(changedNumberOfGuests);

            // when & then
            final var id = savedOrderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.주문테이블_INVALID();

            final var changedNumberOfGuests = 10;
            final var request = OrderTableFixture.테이블요청_손님수_수정_생성(changedNumberOfGuests);

            // when & then
            final var id = orderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이면_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.빈테이블_1명();
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var changedNumberOfGuests = 10;
            final var request = OrderTableFixture.테이블요청_손님수_수정_생성(changedNumberOfGuests);

            // when & then
            final var id = savedOrderTable.getId();
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(id, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
