package kitchenpos.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.OrderService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.application.ProductService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.TableService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.global.exception.KitchenposException;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.support.ServiceTest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.table.ui.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.tablegroup.ui.dto.CreateTableGroupRequest;
import kitchenpos.order.ui.dto.UpdateOrderStateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static kitchenpos.global.exception.ExceptionInformation.*;
import static kitchenpos.support.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 그룹화 서비스 테스트")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 생성 테스트")
    @Nested
    class CreateTableGroup {

        @Test
        void 정상적으로_테이블_그룹을_만든다() {
            // given
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));

            final TableGroup 저장된_테이블그룹 = tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            // then
            assertSoftly(soft -> {
                soft.assertThat(저장된_테이블그룹.getId()).isNotNull();
                soft.assertThat(저장된_테이블그룹.getCreatedDate()).isBefore(LocalDateTime.now());
                soft.assertThat(저장된_테이블그룹.getOrderTables()).hasSize(2);
                soft.assertThat(저장된_테이블그룹.getOrderTables().get(0).isEmpty()).isFalse();
                soft.assertThat(저장된_테이블그룹.getOrderTables().get(1).isEmpty()).isFalse();
                soft.assertThat(저장된_테이블그룹.getOrderTables().get(0).getTableGroupId()).isEqualTo(저장된_테이블그룹.getId());
                soft.assertThat(저장된_테이블그룹.getOrderTables().get(1).getTableGroupId()).isEqualTo(저장된_테이블그룹.getId());
            });
        }

        @Test
        void 테이블이_1개일_경우_예외가_발생한다() {
            final OrderTable 테이블1 = tableService.create(주문_테이블());

            assertThatThrownBy(() -> tableGroupService.create(new CreateTableGroupRequest(List.of(테이블1))))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(TABLE_GROUP_UNDER_BOUNCE.getMessage());
        }

        @Test
        void 테이블이_없을경우_예외가_발생한다() {
            assertThatThrownBy(() -> tableGroupService.create(new CreateTableGroupRequest(Collections.emptyList())))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(TABLE_GROUP_UNDER_BOUNCE.getMessage());
        }

        @Test
        void 중복된_테이블로_테이블_그룹을_만들_수_없다() {
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final var 중복된_테이블_그룹_생성요청 = new CreateTableGroupRequest(List.of(테이블1, 테이블1));

            assertThatThrownBy(() -> tableGroupService.create(중복된_테이블_그룹_생성요청))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_IN_TABLE_GROUP_NOT_FOUND_OR_DUPLICATED.getMessage());
        }

        @Test
        void 존재하지_않는_테이블로_테이블_그룹을_만들_수_없다() {
            final OrderTable 저장_안된_테이블1 = OrderTable.create(1);
            final OrderTable 저장_안된_테이블2 = OrderTable.create(1);

            final var 저장_안된_테이블으로_그룹_생성_요청 = new CreateTableGroupRequest(List.of(저장_안된_테이블1, 저장_안된_테이블2));


            assertThatThrownBy(() -> tableGroupService.create(저장_안된_테이블으로_그룹_생성_요청))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_IN_TABLE_GROUP_NOT_FOUND_OR_DUPLICATED.getMessage());
        }

        @Test
        void 이미_그룹이_지정되어있는_테이블을_다른_그룹에_지정하려하면_예외가_발생한다() {
            // given
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));
            tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            // when
            final OrderTable 테이블3 = tableService.create(주문_테이블());

            // 이미 그룹화 되어있는 테이블을 다른 그룹으로 묶는다
            // then
            assertThatThrownBy(() -> tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블3))))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(TABLE_GROUP_NOT_EMPTY_OR_ALREADY_GROUPED.getMessage());
        }

        @Test
        void 비어있지_않은_테이블을_그룹에_지정하려하면_예외가_발생한다() {
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            테이블2.updateOrderStatus(true);
            final var 테이블그룹 = 그룹화_테이블(List.of(테이블1, 테이블2));

            assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(TABLE_GROUP_NOT_EMPTY_OR_ALREADY_GROUPED.getMessage());
        }
    }

    @DisplayName("테이블 그룹화 해제 테스트")
    @Nested
    class UngroupTables {

        @Test
        void 주문이_없는_테이블_그룹은_정상적으로_테이블_그룹화를_해제할_수_있다() {
            // given
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            tableGroupService.ungroup(저장된_테이블_그룹.getId());

            // when
            final List<OrderTable> 조회한_모든_테이블 = tableService.list();

            // then
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(2);
                soft.assertThat(조회한_모든_테이블.get(0).getTableGroupId()).isNull();
                soft.assertThat(조회한_모든_테이블.get(1).getTableGroupId()).isNull();
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isFalse();
                soft.assertThat(조회한_모든_테이블.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        void 주문_내역있고_주문이_완료되지_않은_테이블을_그룹화_해제하려하면_예외가_발생한다() {
            // given
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            // when
            // 테이블에 주문이 완료되지 않은 내역이 존재한다
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 저장한_메뉴 = menuService.create(메뉴(List.of(상품1), 메뉴그룹));
            orderService.create(주문(테이블1, List.of(저장한_메뉴)));

            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(UNGROUP_NOT_COMPLETED_ORDER_TABLE.getMessage());
        }

        @Test
        void 주문_내역있고_주문이_완료된_테이블은_그룹화_해제_할_수_있다() {
            // given
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            // 테이블에 주문이 존재한다
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 저장한_메뉴 = menuService.create(메뉴(List.of(상품1), 메뉴그룹));
            final Order 저장된_주문 = orderService.create(주문(테이블1, List.of(저장한_메뉴)));

            // 테이블 주문을 완료 시킨다
            final UpdateOrderStateRequest 변경할_주문_상태 = new UpdateOrderStateRequest(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(저장된_주문.getId(), 변경할_주문_상태);

            // when
            tableGroupService.ungroup(저장된_테이블_그룹.getId());
            final List<OrderTable> 조회한_모든_테이블 = tableService.list();

            // then
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(2);
                soft.assertThat(조회한_모든_테이블.get(0).getTableGroupId()).isNull();
                soft.assertThat(조회한_모든_테이블.get(1).getTableGroupId()).isNull();
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isFalse();
                soft.assertThat(조회한_모든_테이블.get(1).isEmpty()).isFalse();
            });
        }
    }
}
