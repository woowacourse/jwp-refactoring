package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    @Nested
    class 테이블_그룹_생성 extends IntegrationTest {

        @Test
        void 요청으로_하나의_테이블로_묶을_수_있다() {
            // given
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));

            // when
            TableGroup extract = tableGroupService.create(
                new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청으로_하나의_테이블로_묶을_때_빈_테이블이_아닌_테이블이_있는경우_예외가_발생한다() {
            // given
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, false));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청으로_하나의_테이블로_묶을때_2개_미만일_경우_예외가_발생한다() {
            // given
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1))))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청으로_하나의_테이블로_묶을때_이미_단체로_지정된_테이블이_속한경우_예외가_발생한다() {
            // given
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // when
            OrderTable orderTable3 = tableService.create(new OrderTable(null, 3, true));
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable3))))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제 extends IntegrationTest {
        @Test
        void 요청으로_지정된_그룹을_해제할_수_있다() {
            // given
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

            // when & then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @Test
        void 요청으로_지정된_그룹을_해제할_때_주문의_상태가_종료가_아닌경우_예외가_발생한다() {
            // given
            MenuGroup menuGroup = menuGroupService.create(new MenuGroup("1인 메뉴"));
            Product product = productService.create(new Product("짜장면", BigDecimal.valueOf(1000)));
            Menu createMenu = new Menu("짜장면", BigDecimal.valueOf(1000), menuGroup.getId());
            createMenu.addMenuProducts(List.of(new MenuProduct(1L, null, product.getId(), 1)));
            Menu saveMenu = menuService.create(createMenu);
            OrderTable orderTable1 = tableService.create(new OrderTable(null, 2, true));
            OrderTable orderTable2 = tableService.create(new OrderTable(null, 3, true));
            TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));
            orderService.create(new Order(orderTable1.getId(), LocalDateTime.now(), List.of(new OrderLineItem(saveMenu.getId(), 1))));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}