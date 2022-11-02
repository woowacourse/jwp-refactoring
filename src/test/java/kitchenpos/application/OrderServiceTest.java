package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.entity.Price;
import kitchenpos.domain.entity.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.jpa.dto.order.OrderCreateRequest;
import kitchenpos.ui.jpa.dto.order.OrderCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(10, false);
        orderTableRepository.save(orderTable);

        MenuGroup menuGroup = new MenuGroup("name");
        menuGroupRepository.save(menuGroup);

        Product product = new Product("name", 1000L);
        productRepository.save(product);

        MenuProduct menuProduct = new MenuProduct(product, 5L);

        Menu menu = new Menu("name", menuGroup, List.of(menuProduct), new Price(5000L));
        menuRepository.save(menu);
        orderLineItem = new OrderLineItem(menu, 1L);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));

        OrderCreateResponse orderCreateResponse = orderService.create(orderCreateRequest);

        assertThat(orderCreateResponse.getId()).isNotNull();
    }

    @DisplayName("orderTable이 empty인 곳으로 주문을 생성할 수 없다.")
    @Test
    void create_Excpetion_OrderTable_Empty() {
        OrderTable orderTableEmpty = new OrderTable(10, true);
        orderTableRepository.save(orderTableEmpty);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableEmpty.getId(), List.of(orderLineItem));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 주문을 조회한다.")
    @Test
    void list() {
        int numberOfOrderBeforeCreate = orderService.list().size();
        orderService.create(new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem)));

        int numberOfOrder = orderService.list().size();

        assertThat(numberOfOrderBeforeCreate + 1).isEqualTo(numberOfOrder);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(String orderStatus) {
        OrderCreateResponse orderCreateResponse = orderService.create(
                new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem)));

        orderService.changeOrderStatus(orderCreateResponse.getId(), orderStatus);

        Order order = orderRepository.findById(orderCreateResponse.getId()).get();
        assertThat(order.getOrderStatus().getValue()).isEqualTo(orderStatus);
    }

    @DisplayName("주문의 상태가 completion이라면 상태 변경을 할 수 없다.")
    @Test
    void changeOrderStatus_Exception_Already_Completion() {
        OrderCreateResponse orderCreateResponse = orderService.create(
                new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem)));
        orderService.changeOrderStatus(orderCreateResponse.getId(), "COMPLETION");

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderCreateResponse.getId(), "MEAL"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
