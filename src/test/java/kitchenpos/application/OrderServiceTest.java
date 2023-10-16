package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static kitchenpos.support.TestFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 서비스 테스트")
@ServiceTest
class OrderServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;


    @Test
    void 전체_주문내역을_조회한다() {
        // given
        final Product 상품1 = productService.create(상품);
        final Product 상품2 = productService.create(상품);
        final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
        final Menu 신메뉴 = 메뉴(List.of(상품1, 상품2), 메뉴그룹);
        final Menu 저장한_신메뉴 = menuService.create(신메뉴);

        final OrderTable 테이블 = tableService.create(new OrderTable());
        final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);

        final Order 새로운_주문 = new Order();
        새로운_주문.setOrderTableId(테이블.getId());
        새로운_주문.setOrderLineItems(List.of(주문1));

        final Order 저장한_주문 = orderService.create(새로운_주문);

        // when
        final List<Order> 조회한_전체_주문 = orderService.list();

        // then
        assertSoftly(soft -> {
            soft.assertThat(조회한_전체_주문).hasSize(1);
            soft.assertThat(조회한_전체_주문.get(0).getId()).isEqualTo(저장한_주문.getId());
        });
    }

    @DisplayName("주문 저장 테스트")
    @Nested
    class createOrder {

        @Test
        void 정상적인_주문을_저장한다() {
            // given
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 신메뉴 = 메뉴(List.of(상품1, 상품2), 메뉴그룹);
            final Menu 저장한_신메뉴 = menuService.create(신메뉴);

            final OrderTable 테이블 = tableService.create(new OrderTable());
            final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());
            새로운_주문.setOrderLineItems(List.of(주문1));

            // when
            final Order 저장한_주문 = orderService.create(새로운_주문);

            // then
            assertSoftly(soft -> {
                soft.assertThat(저장한_주문.getId()).isNotNull();
                soft.assertThat(저장한_주문.getOrderedTime()).isBefore(LocalDateTime.now());
                soft.assertThat(저장한_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
                soft.assertThat(저장한_주문.getOrderTableId()).isEqualTo(테이블.getId());
                soft.assertThat(저장한_주문.getOrderLineItems()).hasSize(1);
                soft.assertThat(저장한_주문.getOrderLineItems().get(0).getOrderId()).isEqualTo(저장한_주문.getId());
                soft.assertThat(저장한_주문.getOrderLineItems().get(0).getSeq()).isNotNull();
            });
        }

        @Test
        void 주문_항목이_비었다면_예외가_발생한다() {
            final OrderTable 테이블 = tableService.create(new OrderTable());

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());
            새로운_주문.setOrderLineItems(Collections.emptyList());

            assertThatThrownBy(() -> orderService.create(새로운_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 비었습니다");
        }

        @Test
        void 없는_메뉴를_주문하려하면_예외가_발생한다() {
            final OrderTable 테이블 = tableService.create(new OrderTable());

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());

            // 존재하지 않는 메뉴를 넣는다.
            새로운_주문.setOrderLineItems(List.of(new OrderLineItem(-1L, 10)));

            assertThatThrownBy(() -> orderService.create(새로운_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목에 중복되거나 존재하지 않는 메뉴가 존재합니다");
        }

        @Test
        void 같은_메뉴를_두개의_주문항목으로_저장하려하면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 신메뉴 = 메뉴(List.of(상품1), 메뉴그룹);
            final Menu 저장한_신메뉴 = menuService.create(신메뉴);

            final OrderTable 테이블 = tableService.create(new OrderTable());

            // 같은 메뉴를 중복해서 주문에 저장한다
            final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);
            final OrderLineItem 주문2 = new OrderLineItem(저장한_신메뉴.getId(), 2);

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());
            새로운_주문.setOrderLineItems(List.of(주문1, 주문2));

            assertThatThrownBy(() -> orderService.create(새로운_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목에 중복되거나 존재하지 않는 메뉴가 존재합니다");
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 신메뉴 = 메뉴(List.of(상품1), 메뉴그룹);
            final Menu 저장한_신메뉴 = menuService.create(신메뉴);

            final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);

            // 존재하지 않는 테이블정보로 주문을 셋팅한다
            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(-1L);
            새로운_주문.setOrderLineItems(List.of(주문1));

            assertThatThrownBy(() -> orderService.create(새로운_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당하는 주문 테이블이 존재하지 않습니다");
        }

        @Test
        void 주문_테이블의_상태가_empty라면_예외가_발생한다() {
            final Product 상품1 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 신메뉴 = 메뉴(List.of(상품1), 메뉴그룹);
            final Menu 저장한_신메뉴 = menuService.create(신메뉴);

            // 테이블의 상태를 emtpy로 설정한다
            final OrderTable table = new OrderTable();
            table.setEmpty(true);
            final OrderTable 테이블 = tableService.create(table);

            final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());
            새로운_주문.setOrderLineItems(List.of(주문1));

            assertThatThrownBy(() -> orderService.create(새로운_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 상태가 empty입니다");
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class changeOrderStatus {

        private Order 주문;

        @BeforeEach
        void 주문을_1건_넣는다() {
            final Product 상품1 = productService.create(상품);
            final Product 상품2 = productService.create(상품);
            final MenuGroup 메뉴그룹 = menuGroupService.create(메뉴_분류);
            final Menu 신메뉴 = 메뉴(List.of(상품1, 상품2), 메뉴그룹);
            final Menu 저장한_신메뉴 = menuService.create(신메뉴);

            final OrderTable 테이블 = tableService.create(new OrderTable());
            final OrderLineItem 주문1 = new OrderLineItem(저장한_신메뉴.getId(), 1);

            final Order 새로운_주문 = new Order();
            새로운_주문.setOrderTableId(테이블.getId());
            새로운_주문.setOrderLineItems(List.of(주문1));

            주문 = orderService.create(새로운_주문);
        }

        @Test
        void 정상적으로_주문_상태를_변경한다() {
            final Order 완료된_주문 = new Order();
            완료된_주문.setOrderStatus(OrderStatus.COMPLETION.name());
            final Order 상태가_변경된_주문 = orderService.changeOrderStatus(주문.getId(), 완료된_주문);

            Assertions.assertThat(상태가_변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        }

        @Test
        void 저장되어있는_주문_상태가_COMPLETE면_상태를_변경할_수_없다() {
            final Order 완료된_주문 = new Order();
            완료된_주문.setOrderStatus(OrderStatus.COMPLETION.name());
            orderService.changeOrderStatus(주문.getId(), 완료된_주문);

            final Order 다시_상태를_바꾸는_주문 = new Order();
            완료된_주문.setOrderStatus(OrderStatus.COOKING.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 다시_상태를_바꾸는_주문))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("완료된 주문의 상태를 변경할 수 없습니다");
        }
    }
}
