package kitchenpos.application;

import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.NotExistOrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    
    private final TableGroupRepository tableGroupRepository;
    
    private final OrderTableRepository orderTableRepository;
    
    private final OrderRepository orderRepository;
    
    public TableService(final TableGroupRepository tableGroupRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }
    
    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final TableGroup tableGroup = tableGroupRepository.findById(request.getTableGroupId())
                                                          .orElseGet(()->null);
        final OrderTable orderTable = new OrderTable(tableGroup,
                request.getNumberOfGuests(),
                request.isEmpty());
        return orderTableRepository.save(orderTable);
    }
    
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }
    
    @Transactional
    public OrderTable changeEmpty(final Long orderTableId,
                                  final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블 입니다"));
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        savedOrderTable.changeEmpty(orders, request.isEmpty());
        return savedOrderTable;
    }
    
    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuests request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블 입니다"));
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return savedOrderTable;
    }
}
