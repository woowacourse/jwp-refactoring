package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProduct.MenuProductBuilder;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTable.OrderTableBuilder;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.NotChangeCompletionStatusException;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderEmptyException;
import kitchenpos.exception.OrderTableEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private static OrderTable savedOrderTable;
    private static Menu savedMenu;
    private static Menu savedMenu2;

    @BeforeEach
    void setUp() {
        OrderTable orderTable = new OrderTableBuilder()
                .setNumberOfGuests(4)
                .setEmpty(false)
                .build();

        savedOrderTable = orderTableRepository.save(orderTable);

        Product product = new Product.ProductBuilder()
                .setName("떡볶이")
                .setPrice(3000)
                .build();

        Product savedProduct = productRepository.save(product);

        MenuProduct menuProduct = new MenuProductBuilder()
                .setQuantity(2)
                .setProduct(savedProduct)
                .build();

        MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);

        Menu menu = new Menu.MenuBuilder()
                .setName("떡볶이2개")
                .setPrice(5000)
                .setMenuProducts(List.of(savedMenuProduct))
                .build();

        savedMenu = menuRepository.save(menu);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given

        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemRequests);

        //when
        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        //then

        System.out.println(orderResponse.getId());
        System.out.println(orderResponse.getOrderTableResponse().getNumberOfGuests());

        assertThat(orderResponse.getOrderTableResponse().getNumberOfGuests()).isEqualTo(4);
        assertThat(orderResponse.getOrderTableResponse().isEmpty()).isFalse();
    }

    @DisplayName("주문 등록 실패 - 비어 있는 주문 항목")
    @Test
    void createFailEmptyOrderLineItem() {
        //given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), Collections
                .emptyList());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(OrderEmptyException.class);
    }

    @DisplayName("주문 등록 실패 - 존재하지 않는 메뉴가 주문에 포함되어 있을 경우")
    @Test
    void createFailNotExistMenu() {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(1000L, 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemRequests);
        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(NotFoundMenuException.class);
    }

    @DisplayName("주문 등록 실패 - 존재하지 않는 주문 테이블")
    @Test
    void createFailNotExistOrderTable() {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1000L, orderLineItemRequests);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @DisplayName("주문 등록 실패 - 주문 테이블이 비어있을 경우")
    @Test
    void createFailEmptyOrderTable() {
        //given
        OrderTable emptyOrderTable = new OrderTableBuilder()
                .setNumberOfGuests(4)
                .setEmpty(true)
                .build();

        OrderTable savedEmptyOrderTable = orderTableRepository.save(emptyOrderTable);

        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedEmptyOrderTable.getId(), orderLineItemRequests);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @DisplayName("주문 목록을 불러온다.")
    @Test
    void list() {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemRequests);

        orderService.create(orderCreateRequest);
        orderService.create(orderCreateRequest);

        //when
        List<OrderResponse> orderResponses = orderService.list();
        //then
        assertThat(orderResponses).hasSize(2);
    }

    @DisplayName("주문 상태를 변경한다.")
    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    void changeOrderStatus(String status) {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemRequests);

        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        //when
        OrderResponse actual = orderService
                .changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(status));
        //then
        assertThat(actual.getOrderStatus()).isEqualTo(status);
    }

    @DisplayName("주문 상태를 변경 실패 - 주문 상태가 이미 계산 완료일 경우")
    @Test
    void changeOrderStatusFailAlreadyCompletionStatus() {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = List
                .of(new OrderLineItemRequest(savedMenu.getId(), 1L));

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemRequests);

        OrderResponse orderResponse = orderService.create(orderCreateRequest);
        orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.COMPLETION
                .name()));
        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.COMPLETION
                .name())))
                .isInstanceOf(NotChangeCompletionStatusException.class);
    }
}
