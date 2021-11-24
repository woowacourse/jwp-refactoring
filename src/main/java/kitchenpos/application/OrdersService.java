package kitchenpos.application;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;
import static kitchenpos.domain.OrderStatus.COMPLETION;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.application.dto.OrdersDtoAssembler;
import kitchenpos.application.dto.request.OrderLineItemRequestDto;
import kitchenpos.application.dto.request.OrdersRequestDto;
import kitchenpos.application.dto.request.OrdersStatusRequestDto;
import kitchenpos.application.dto.response.OrdersResponseDto;
import kitchenpos.application.dto.response.OrdersStatusResponseDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrdersRepository;
import kitchenpos.domain.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final MenuRepository menuRepository;
    private final OrdersRepository ordersRepository;
    private final TableRepository tableRepository;

    public OrdersService(
        EntityManager entityManager,
        MenuRepository menuRepository,
        OrdersRepository ordersRepository,
        TableRepository tableRepository
    ) {
        this.entityManager = entityManager;
        this.menuRepository = menuRepository;
        this.ordersRepository = ordersRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrdersResponseDto create(OrdersRequestDto requestDto) {
        List<OrderLineItemRequestDto> orderLineItemsRequestDto = requestDto.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItemsRequestDto)) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequestDto orderLineItemRequestDto : orderLineItemsRequestDto) {
            Menu menu = menuRepository.findById(orderLineItemRequestDto.getMenuId())
                .orElseThrow(IllegalArgumentException::new);

            orderLineItems.add(new OrderLineItem(orderLineItemRequestDto.getQuantity(), menu));
        }

        OrderTable orderTable = tableRepository.findById(requestDto.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (TRUE.equals(orderTable.getEmpty())) {
            throw new IllegalArgumentException();
        }

        Orders orders = ordersRepository.save(new Orders(orderTable, orderLineItems));

        return OrdersDtoAssembler.ordersResponseDto(orders);
    }

    public List<OrdersResponseDto> list() {
        List<Orders> orders = ordersRepository.findAll();

        return orders.stream()
            .map(OrdersDtoAssembler::ordersResponseDto)
            .collect(toList());
    }

    @Transactional
    public OrdersStatusResponseDto changeOrderStatus(OrdersStatusRequestDto requestDto) {
        Orders orders = ordersRepository.findById(requestDto.getOrderId())
            .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(COMPLETION.name(), orders.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        orders.changeOrderStatus(requestDto.getOrderStatus());

        entityManager.flush();

        return OrdersDtoAssembler.ordersStatusResponseDto(orders);
    }
}
