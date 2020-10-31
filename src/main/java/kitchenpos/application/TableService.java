package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.enums.OrderStatus;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeGuestsRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest tableCreateRequest) {
        final OrderTable orderTable = tableCreateRequest.toEntity();

        return TableResponse.of(orderTableRepository.save(orderTable));
    }

    public List<TableResponse> list() {
        return TableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyRequest tableChangeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을수가 없습니다."));

        savedOrderTable.existTableGroupId();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중이면 상태를 변경할 수 없습니다.");
        }

        savedOrderTable.changeEmpty(tableChangeEmptyRequest.isEmpty());

        final OrderTable orderTable = orderTableRepository.save(savedOrderTable);

        return TableResponse.of(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId,
            final TableChangeGuestsRequest tableChangeGuestsRequest) {
        final int numberOfGuests = tableChangeGuestsRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 테이블을 찾을수 없습니다."));

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        final OrderTable orderTable = orderTableRepository.save(savedOrderTable);

        return TableResponse.of(orderTable);
    }
}
