package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        final OrderTable 새테이블 = tableService.create(new OrderTable());

        assertSoftly(soft -> {
            soft.assertThat(새테이블.getId()).isNotNull();
            soft.assertThat(새테이블.getTableGroupId()).isNull();
            soft.assertThat(새테이블.getNumberOfGuests()).isZero();
            soft.assertThat(새테이블.isEmpty()).isFalse();
        });
    }

    @Test
    void 테이블의_아이디_상태_그룹아이디_손님수를_지정해서_만든다() {
        final OrderTable 테이블 = new OrderTable();
        테이블.setId(-1L);
        테이블.setEmpty(true);
        테이블.setTableGroupId(1L);
        테이블.setNumberOfGuests(1);

        final OrderTable 저장한_새테이블 = tableService.create(테이블);

        assertSoftly(soft -> {
            soft.assertThat(저장한_새테이블.getId()).isNotNull();
            soft.assertThat(저장한_새테이블.getId()).isNotEqualTo(-1L);
            soft.assertThat(저장한_새테이블.getTableGroupId()).isNull();
            soft.assertThat(저장한_새테이블.getNumberOfGuests()).isEqualTo(1);
            soft.assertThat(저장한_새테이블.isEmpty()).isTrue();
        });
    }

    @Test
    void 전체_테이블을_조회한다() {
        tableService.create(new OrderTable());
        final List<OrderTable> 조회한_모든_테이블 = tableService.list();

        assertThat(조회한_모든_테이블).hasSize(1);
    }

    @DisplayName("테이블의 empty 상태 변경 테스트")
    @Nested
    class changeEmpty {
        @Test
        void 정상적으로_테이블의_상태를_주문테이블에서_빈테이블로_변경한다() {
            final OrderTable 주문_테이블 = tableService.create(new OrderTable());

            final OrderTable 빈_테이블 = new OrderTable();
            빈_테이블.setEmpty(true);
            tableService.changeEmpty(주문_테이블.getId(), 빈_테이블);

            final List<OrderTable> 조회한_모든_테이블 = tableService.list();
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(1);
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isTrue();
            });
        }

        @Test
        void 정상적으로_테이블의_상태를_빈테이블에서_주문테이블로_변경한다() {
            final OrderTable 빈_테이블 = new OrderTable();
            빈_테이블.setEmpty(true);
            final OrderTable 저장한_빈_테이블 = tableService.create(빈_테이블);

            final OrderTable 주문_테이블 = new OrderTable();
            tableService.changeEmpty(저장한_빈_테이블.getId(), 주문_테이블);

            final List<OrderTable> 조회한_모든_테이블 = tableService.list();
            assertSoftly(soft -> {
                soft.assertThat(조회한_모든_테이블).hasSize(1);
                soft.assertThat(조회한_모든_테이블.get(0).isEmpty()).isFalse();
            });
        }

        @Test
        void 존재하지_않는_테이블을_변경하면_예외가_발생한다() {
            assertThatThrownBy(() -> tableService.changeEmpty(-1L, new OrderTable()))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 테이블이 존재하지 않습니다.");
        }

        @Test
        void 변경하려는_테이블에_그룹아이디가_지정되어있으면_예외가_발생한다() {
            final OrderTable 테이블1 = tableService.create(주문_테이블());
            final OrderTable 테이블2 = tableService.create(주문_테이블());
            tableGroupService.create(그룹화_테이블(List.of(테이블1, 테이블2)));

            final OrderTable 빈테이블 = new OrderTable();
            빈테이블.setEmpty(true);

            assertThatThrownBy(() -> tableService.changeEmpty(테이블1.getId(), 빈테이블))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 테이블의 그룹테이블 아이디가 존재합니다");
        }

        @Test
        void 변경하려는_테이블의_주문내역이_존재하며_주문상태가_조리중이나_식사중이면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 저장한_메뉴 = menuService.create(메뉴(List.of(상품1), 메뉴그룹));
            final OrderTable 테이블 = tableService.create(new OrderTable());

            // 해당 테이블에 상태가 조리중인 주문 상태가 된다.
            orderService.create(주문(테이블, List.of(저장한_메뉴)));

            final OrderTable 변경할_주문_테이블상태 = new OrderTable();
            변경할_주문_테이블상태.setEmpty(false);

            assertThatThrownBy(() -> tableService.changeEmpty(테이블.getId(), 변경할_주문_테이블상태))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 테이블의 주문 상태는 COMPLETION여야 합니다.");
        }
    }

    @DisplayName("테이블의 손님 수 변경 테스트")
    @Nested
    class changeNumberOfGuests {

        @Test
        void 정상적으로_테이블_손님수를_변경한다() {
            final OrderTable 저장된_테이블 = tableService.create(new OrderTable());
            final OrderTable 손님수를_변경하는_테이블 = new OrderTable();
            손님수를_변경하는_테이블.setNumberOfGuests(10);

            final OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(저장된_테이블.getId(), 손님수를_변경하는_테이블);

            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        void 손님_수가_음수이면_예외가_발생한다() {
            final OrderTable 저장된_테이블 = tableService.create(new OrderTable());

            final OrderTable 손님수를_변경하는_테이블 = new OrderTable();
            손님수를_변경하는_테이블.setNumberOfGuests(-1);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(저장된_테이블.getId(), 손님수를_변경하는_테이블))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("손님 수는 음수일 수 없습니다.");
        }

        @Test
        void 존재하지_않는_테이블의_손님수를_변경하면_예외가_발생한다() {
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTable()))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 테이블이 존재하지 않습니다.");
        }

        @Test
        void 변경하려는_테이블의_emtpty가_true면_예외가_발생한다() {
            final OrderTable EMPTY_상태의_테이블 = new OrderTable();
            EMPTY_상태의_테이블.setEmpty(true);
            final OrderTable 저장된_테이블 = tableService.create(EMPTY_상태의_테이블);

            final OrderTable 손님수를_변경하는_테이블 = new OrderTable();
            손님수를_변경하는_테이블.setNumberOfGuests(10);

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(저장된_테이블.getId(), 손님수를_변경하는_테이블))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("변경하려는 테이블의 상태가 empty일시 변경할 수 없습니다");
        }
    }

}
