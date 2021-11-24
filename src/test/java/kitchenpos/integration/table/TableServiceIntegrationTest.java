package kitchenpos.integration.table;

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
import kitchenpos.application.dto.request.TableEmptyRequestDto;
import kitchenpos.application.dto.request.TableNumberOfGuestsRequestDto;
import kitchenpos.application.dto.request.TableRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.application.dto.response.TableEmptyResponseDto;
import kitchenpos.application.dto.response.TableNumberOfGuestsResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceIntegrationTest extends IntegrationTest {

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

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create_Valid_Success() {
        // given
        TableRequestDto requestDto = new TableRequestDto(2L, FALSE);

        // when
        TableResponseDto responseDto = tableService.create(requestDto);

        // then
        assertThat(responseDto.getId()).isNotNull();
    }

    @DisplayName("테이블의 목록을 조회할 수 있다.")
    @Test
    void list_Valid_Success() {
        // given
        TableRequestDto requestDto = new TableRequestDto(2L, FALSE);

        tableService.create(requestDto);

        // when
        List<TableResponseDto> responsesDto = tableService.list();

        // then
        assertThat(responsesDto).hasSize(1);
    }

    @DisplayName("테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty_Valid_Success() {
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
        OrdersStatusResponseDto ordersStatusResponseDto = ordersService
            .changeOrderStatus(ordersStatusRequestDto);

        TableEmptyRequestDto requestDto = new TableEmptyRequestDto(
            ordersStatusResponseDto.getOrderTableId(),
            TRUE
        );

        // when
        TableEmptyResponseDto responseDto = tableService.changeEmpty(requestDto);

        // then
        assertThat(responseDto.getEmpty()).isTrue();
    }

    @DisplayName("테이블 상태는 주문 상태가 `조리`면 변경할 수 없다.")
    @Test
    void changeEmpty_InvalidCookingOrderStatus_Fail() {
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
        ordersService.create(ordersRequestDto);

        TableEmptyRequestDto requestDto = new TableEmptyRequestDto(
            tableResponseDto.getId(),
            TRUE
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 상태는 주문 상태가 `식사`면 변경할 수 없다.")
    @Test
    void changeEmpty_InvalidMealOrderStatus_Fail() {
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
            MEAL.name()
        );
        ordersService.changeOrderStatus(ordersStatusRequestDto);

        TableEmptyRequestDto requestDto = new TableEmptyRequestDto(
            tableResponseDto.getId(),
            TRUE
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests_Valid_Success() {
        // given
        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        TableNumberOfGuestsRequestDto requestDto = new TableNumberOfGuestsRequestDto(
            tableResponseDto.getId(),
            4L
        );

        // when
        TableNumberOfGuestsResponseDto responseDto = tableService
            .changeNumberOfGuests(requestDto);

        // then
        assertThat(responseDto.getNumberOfGuests()).isEqualTo(4L);
    }

    @DisplayName("테이블에 방문한 손님수는 주문 테이블이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_NonExistingOrderTable_Fail() {
        // given
        TableNumberOfGuestsRequestDto requestDto = new TableNumberOfGuestsRequestDto(
            Long.MAX_VALUE,
            4L
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수는 주문 테이블이 비어 있으면(손님이 없으면) 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_EmptyOrderTable_Fail() {
        // given
        TableRequestDto tableRequestDto = new TableRequestDto(0L, TRUE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        TableNumberOfGuestsRequestDto requestDto = new TableNumberOfGuestsRequestDto(
            tableResponseDto.getId(),
            4L
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님수가 올바르지 않으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_Negative_Fail() {
        // given
        TableRequestDto tableRequestDto = new TableRequestDto(2L, FALSE);
        TableResponseDto tableResponseDto = tableService.create(tableRequestDto);

        TableNumberOfGuestsRequestDto requestDto = new TableNumberOfGuestsRequestDto(
            tableResponseDto.getId(),
            -2L
        );

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestDto))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
