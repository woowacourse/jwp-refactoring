package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.KitchenposException;
import kitchenpos.support.ServiceTest;
import kitchenpos.ui.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.request.ChangeOrderTableGuestRequest;
import kitchenpos.ui.dto.request.CreateOrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.exception.ExceptionInformation.*;
import static kitchenpos.support.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 서비스 테스트")
@ServiceTest
class TableServiceTest {

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

    @Test
    void 아무_설정없는_기본_테이블을_만든다() {
        final OrderTable 새테이블 = tableService.create(new CreateOrderTableRequest());

        assertSoftly(soft -> {
            soft.assertThat(새테이블.getId()).isNotNull();
            soft.assertThat(새테이블.getTableGroupId()).isNull();
            soft.assertThat(새테이블.getNumberOfGuests()).isZero();
            soft.assertThat(새테이블.isEmpty()).isFalse();
        });
    }

    @Test
    void 테이블의_손님수를_지정해서_만든다() {
        final CreateOrderTableRequest 테이블 = new CreateOrderTableRequest(1);
        테이블.setNumberOfGuests(1);

        final OrderTable 저장한_새테이블 = tableService.create(테이블);

        assertSoftly(soft -> {
            soft.assertThat(저장한_새테이블.getId()).isNotNull();
            soft.assertThat(저장한_새테이블.getId()).isNotEqualTo(-1L);
            soft.assertThat(저장한_새테이블.getTableGroupId()).isNull();
            soft.assertThat(저장한_새테이블.getNumberOfGuests()).isEqualTo(1);
            soft.assertThat(저장한_새테이블.isEmpty()).isFalse();
        });
    }

    @Test
    void 전체_테이블을_조회한다() {
        tableService.create(new CreateOrderTableRequest());
        final List<OrderTable> 조회한_모든_테이블 = tableService.list();

        assertThat(조회한_모든_테이블).hasSize(1);
    }

    @DisplayName("테이블의 empty 상태 변경 테스트")
    @Nested
    class changeEmpty {
        @Test
        void 정상적으로_테이블의_상태를_주문테이블에서_빈테이블로_변경한다() {
            final OrderTable 주문_테이블 = tableService.create(new CreateOrderTableRequest());
            tableService.changeEmpty(주문_테이블.getId(), new ChangeOrderTableEmptyRequest(true));

            final List<OrderTable> 조회한_모든_테이블 = tableService.list();
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(1);
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isTrue();
            });
        }

        @Test
        void 정상적으로_테이블의_상태를_빈테이블에서_주문테이블로_변경한다() {
            final OrderTable 테이블 = tableService.create(new CreateOrderTableRequest());
            tableService.changeEmpty(테이블.getId(), new ChangeOrderTableEmptyRequest(true));

            // 빈 테이블을 주문테이블로 변경한다
            tableService.changeEmpty(테이블.getId(), new ChangeOrderTableEmptyRequest(false));

            final List<OrderTable> 조회한_모든_테이블 = tableService.list();
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(1);
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isFalse();
            });
        }

        @Test
        void 존재하지_않는_테이블을_변경하면_예외가_발생한다() {
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, new ChangeOrderTableEmptyRequest()))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_NOT_FOUND.getMessage());
        }

        @Test
        void 변경하려는_테이블에_그룹아이디가_지정되어있으면_예외가_발생한다() {
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());

            tableService.changeEmpty(테이블1.getId(), new ChangeOrderTableEmptyRequest(true));
            tableService.changeEmpty(테이블2.getId(), new ChangeOrderTableEmptyRequest(true));

            tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            final ChangeOrderTableEmptyRequest 빈테이블로_변경하는_요청 = new ChangeOrderTableEmptyRequest(true);

            assertThatThrownBy(() -> tableService.changeEmpty(테이블1.getId(), 빈테이블로_변경하는_요청))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_IS_GROUPING.getMessage());
        }

        @Test
        void 변경하려는_테이블의_주문내역이_존재하며_주문상태가_조리중이나_식사중이면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 저장한_메뉴 = menuService.create(메뉴(List.of(상품1), 메뉴그룹));
            final OrderTable 테이블 = tableService.create(주문_테이블());

            // 해당 테이블에 상태가 조리중인 주문 상태가 된다.
            orderService.create(주문(테이블, List.of(저장한_메뉴)));
            final var 변경할_주문_테이블상태 = new ChangeOrderTableEmptyRequest(false);

            assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), 변경할_주문_테이블상태))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_STATUS_IS_NOT_COMPLETE.getMessage());
        }
    }

    @DisplayName("테이블의 손님 수 변경 테스트")
    @Nested
    class changeNumberOfGuests {

        @Test
        void 정상적으로_테이블_손님수를_변경한다() {
            final OrderTable 저장된_테이블 = tableService.create(주문_테이블());
            final OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(저장된_테이블.getId(), new ChangeOrderTableGuestRequest(10));

            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        void 손님_수가_음수이면_예외가_발생한다() {
            final OrderTable 저장된_테이블 = tableService.create(주문_테이블());

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(저장된_테이블.getId(), new ChangeOrderTableGuestRequest(-1)))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_GUEST_OUT_OF_BOUNCE.getMessage());
        }

        @Test
        void 존재하지_않는_테이블의_손님수를_변경하면_예외가_발생한다() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new ChangeOrderTableGuestRequest()))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(ORDER_TABLE_NOT_FOUND.getMessage());
        }

        @Test
        void 변경하려는_테이블의_emtpty가_true면_예외가_발생한다() {
            final OrderTable 테이블 = tableService.create(new CreateOrderTableRequest(0));
            tableService.changeEmpty(테이블.getId(), new ChangeOrderTableEmptyRequest(true));

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(테이블.getId(), new ChangeOrderTableGuestRequest(10)))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(EMPTY_TABLE_UPDATE_GUEST.getMessage());
        }
    }

}
