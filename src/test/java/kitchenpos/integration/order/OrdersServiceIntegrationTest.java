package kitchenpos.integration.order;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;
import kitchenpos.application.dto.request.OrdersRequestDto;
import kitchenpos.application.dto.request.OrdersStatusRequestDto;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.request.TableRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrdersServiceIntegrationTest extends IntegrationTest {

    private static final Long QUANTITY = 2L;

    private MenuRequestDto menuRequestDto;

    @BeforeEach
    void setUp() {
        ProductResponseDto productResponseDto = productService
            .create(new ProductRequestDto("얌 프라이", BigDecimal.valueOf(8000)));
        MenuGroupResponseDto menuGroupResponseDto = menuGroupService
            .create(new MenuGroupRequestDto("시즌 메뉴"));

        MenuProductRequestDto menuProduct = new MenuProductRequestDto(
            productResponseDto.getId(),
            QUANTITY
        );
        menuRequestDto = new MenuRequestDto(
            "얌 프라이",
            BigDecimal.valueOf(8000),
            menuGroupResponseDto.getId(),
            Collections.singletonList(menuProduct)
        );
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto requestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        // when
        OrdersResponseDto responseDto = ordersService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
    }

    @DisplayName("주문의 주문 항목이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingMenuInOrderLineItems_Fail() {
        // given
        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrdersRequestDto requestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.emptyList()
        );

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 항목에 메뉴가 하나라도 적혀있지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderLineItems_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            Long.MAX_VALUE,
            QUANTITY
        );
        OrdersRequestDto requestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 존재하지 않으면 등록할 수 없다.")
    @Test
    void create_NonExistingOrderTable_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto requestDto = new OrdersRequestDto(
            Long.MAX_VALUE,
            Collections.singletonList(orderLineItemRequestDto)
        );

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 주문 테이블이 비어 있으면(손님이 없으면) 등록할 수 없다.")
    @Test
    void create_NonExistingGuestsInOrderTable_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, TRUE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto requestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        // when
        // then
        assertThatThrownBy(() -> ordersService.create(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void read_Valid_Success() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto requestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        ordersService.create(requestDto);

        // when
        List<OrdersResponseDto> responsesDto = ordersService.list();

        // then
        assertThat(responsesDto).hasSize(1);
    }


    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus_Valid_Success() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto ordersRequestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        OrdersResponseDto ordersResponseDto = ordersService.create(ordersRequestDto);

        OrdersStatusRequestDto requestDto = new OrdersStatusRequestDto(
            ordersResponseDto.getId(),
            MEAL.name()
        );

        // when
        OrdersStatusResponseDto responseDto = ordersService.changeOrderStatus(requestDto);

        // then
        assertThat(responseDto.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("주문 상태는 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatus_NonExistingOrder_Fail() {
        // given
        OrdersStatusRequestDto requestDto = new OrdersStatusRequestDto(
            Long.MAX_VALUE,
            MEAL.name()
        );

        // when
        // then
        assertThatThrownBy(() -> ordersService.changeOrderStatus(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 `계산 완료`면 변경할 수 없다.")
    @Test
    void changeOrderStatus_InvalidOrderStatus_Fail() {
        // given
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);

        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        OrderLineItemRequestDto orderLineItemRequestDto = new OrderLineItemRequestDto(
            menuResponseDto.getId(),
            QUANTITY
        );
        OrdersRequestDto ordersRequestDto = new OrdersRequestDto(
            tableResponseDto.getId(),
            Collections.singletonList(orderLineItemRequestDto)
        );

        OrdersResponseDto ordersResponseDto = ordersService.create(ordersRequestDto);

        OrdersStatusRequestDto ordersStatusRequestDto = new OrdersStatusRequestDto(
            ordersResponseDto.getId(),
            COMPLETION.name()
        );

        ordersService.changeOrderStatus(ordersStatusRequestDto);

        OrdersStatusRequestDto requestDto = new OrdersStatusRequestDto(
            ordersResponseDto.getId(), MEAL.name());

        // when
        // then
        assertThatThrownBy(() -> ordersService.changeOrderStatus(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
