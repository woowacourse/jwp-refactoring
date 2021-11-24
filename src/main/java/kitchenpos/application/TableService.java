package kitchenpos.application;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.application.dto.TableDtoAssembler;
import kitchenpos.application.dto.request.TableEmptyRequestDto;
import kitchenpos.application.dto.request.TableNumberOfGuestsRequestDto;
import kitchenpos.application.dto.request.TableRequestDto;
import kitchenpos.application.dto.response.TableEmptyResponseDto;
import kitchenpos.application.dto.response.TableNumberOfGuestsResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.OrdersRepository;
import kitchenpos.domain.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final TableRepository tableRepository;
    private final OrdersRepository ordersRepository;

    public TableService(
        EntityManager entityManager,
        TableRepository tableRepository,
        OrdersRepository ordersRepository
    ) {
        this.entityManager = entityManager;
        this.tableRepository = tableRepository;
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public TableResponseDto create(TableRequestDto requestDto) {
        OrderTable orderTable = tableRepository
            .save(new OrderTable(requestDto.getNumberOfGuests(), requestDto.getEmpty()));

        return TableDtoAssembler.tableResponseDto(orderTable);
    }

    public List<TableResponseDto> list() {
        List<OrderTable> orderTables = tableRepository.findAll();

        return orderTables.stream()
            .map(TableDtoAssembler::tableResponseDto)
            .collect(toList());
    }

    @Transactional
    public TableEmptyResponseDto changeEmpty(TableEmptyRequestDto requestDto) {
        OrderTable orderTable = tableRepository.findById(requestDto.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        Orders orders = ordersRepository.findById(requestDto.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        String orderStatus = orders.getOrderStatus();
        if (COOKING.name().equals(orderStatus) || MEAL.name().equals(orderStatus)) {
            throw new IllegalArgumentException();
        }

        orderTable.changeEmpty(requestDto.getEmpty());

        entityManager.flush();

        return TableDtoAssembler.tableEmptyResponseDto(orderTable);
    }

    @Transactional
    public TableNumberOfGuestsResponseDto changeNumberOfGuests(
        TableNumberOfGuestsRequestDto requestDto
    ) {
        OrderTable orderTable = tableRepository.findById(requestDto.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        if (TRUE.equals(orderTable.getEmpty())) {
            throw new IllegalArgumentException();
        }

        orderTable.changeNumberOfGuests(requestDto.getNumberOfGuests());

        entityManager.flush();

        return TableDtoAssembler.tableNumberOfGuestsResponseDto(orderTable);
    }
}
