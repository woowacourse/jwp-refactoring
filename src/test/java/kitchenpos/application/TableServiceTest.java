package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 테이블을_생성할_수_있다() {
        OrderTableRequest request = new OrderTableRequest(5, false);

        OrderTableResponse actual = tableService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void 전체_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 3, false);
        OrderTable orderTable2 = new OrderTable(null, 5, false);

        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 기존_테이블의_빈_테이블_여부를_변경할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, false));
        OrderTableRequest request = new OrderTableRequest(0, true);

        OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(actual.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 기존_테이블의_주문_상태가_완료_상태가_아니면_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
        Product product = productRepository.save(new Product("상품", new BigDecimal(10000)));
        MenuProduct menuProduct = new MenuProduct(product, 1);
        Long menuGroupId = menuGroupRepository.save(new MenuGroup("메뉴 그룹1")).getId();
        Menu menu = menuRepository.save(new Menu("메뉴1", new BigDecimal(10000), menuGroupId, List.of(menuProduct)));

        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);

        Long orderTableId = orderTableRepository.save(new OrderTable(null, 5, false)).getId();
        orderRepository.save(new Order(orderTableId, orderStatus, List.of(orderLineItem)));

        OrderTableRequest request = new OrderTableRequest(0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 기존_테이블의_손님_수를_변경할_수_있다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        OrderTableRequest request = new OrderTableRequest(5, false);

        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 손님_수가_음수인_경우_손님_수를_변경할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, false));
        OrderTableRequest request = new OrderTableRequest(-1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블은_손님_수를_변경할_수_없다() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 0, true));
        OrderTableRequest request = new OrderTableRequest(5, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
