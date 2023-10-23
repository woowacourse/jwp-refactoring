package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;
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
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_단체_지정 {

        @Test
        void 테이블들을_단체_지정할_수_있다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var request = TableGroupFixture.단체지정요청_생성(savedOrderTables);

            // when
            final var actual = tableGroupService.create(request);

            // then
            final var orderTableResponse1 = OrderTableResponse.toResponse(orderTable1);
            final var orderTableResponse2 = OrderTableResponse.toResponse(orderTable2);
            final var expected = List.of(orderTableResponse1, orderTableResponse2);
            assertThat(actual.getOrderTables()).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @Test
        void 한_개_이하의_주문_테이블을_단체_지정할_경우_예외가_발생한다() {
            // given
            final var orderTable = OrderTableFixture.빈테이블_1명();
            final var savedOrderTable = 단일_주문테이블_저장(orderTable);

            final var request = TableGroupFixture.단체지정요청_생성(Collections.singletonList(savedOrderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정하려는_주문_테이블_중_실제_주문_테이블에_존재하지_않는_테이블이_있으면_예외가_발생한다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTable = 단일_주문테이블_저장(orderTable1);

            final var request = TableGroupFixture.단체지정요청_생성(List.of(savedOrderTable, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이_아니면_예외가_발생한다() {
            // given
            final var orderTable1 = OrderTableFixture.주문테이블_N명(1);
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var request = TableGroupFixture.단체지정요청_생성(savedOrderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블들을_빈_테이블에서_주문_테이블로_설정한다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var request = TableGroupFixture.단체지정요청_생성(savedOrderTables);

            // when
            final var actual = tableGroupService.create(request);

            // then
            for (final var orderTable : actual.getOrderTables()) {
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }
    }

    @Nested
    class 테이블_단체_지정_해제 {

        @Test
        void 단체_지정을_해제할_수_있다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var tableGroup = TableGroupFixture.단체지정_여러_테이블(savedOrderTables);
            final var savedTableGroup = 단일_단체지정_저장(tableGroup);

            // when & then
            assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
        }

        @Test
        void 테이블들의_주문_상태가_조리_또는_식사면_예외가_발생한다() {
            // given
            final var orderTable1 = OrderTableFixture.빈테이블_1명();
            final var orderTable2 = OrderTableFixture.빈테이블_1명();
            final var savedOrderTables = 복수_주문테이블_저장(orderTable1, orderTable2);

            final var tableGroup = TableGroupFixture.단체지정_여러_테이블(savedOrderTables);
            final var savedTableGroup = 단일_단체지정_저장(tableGroup);

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

            final var order = 단일_주문_저장(orderTable1, orderLineItem);
            order.changeOrderStatus(MEAL);

            // when & then
            final var id = tableGroup.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(id))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
