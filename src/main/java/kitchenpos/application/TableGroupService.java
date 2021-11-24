package kitchenpos.application;

import static java.lang.Boolean.FALSE;
import static java.util.stream.Collectors.toList;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.application.dto.TableGroupDtoAssembler;
import kitchenpos.application.dto.request.TableGroupIdRequestDto;
import kitchenpos.application.dto.request.TableGroupRequestDto;
import kitchenpos.application.dto.request.TableIdRequestDto;
import kitchenpos.application.dto.response.TableGroupResponseDto;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrdersRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final OrdersRepository ordersRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        EntityManager entityManager,
        OrdersRepository ordersRepository,
        TableRepository tableRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.entityManager = entityManager;
        this.ordersRepository = ordersRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponseDto create(TableGroupRequestDto requestDto) {
        List<Long> orderTableIds = requestDto.getOrderTables().stream()
            .map(TableIdRequestDto::getId)
            .collect(toList());
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        List<OrderTable> orderTables = tableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (FALSE.equals(orderTable.getEmpty()) ||
                Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));

        return TableGroupDtoAssembler.tableGroupResponseDto(tableGroup);
    }

    @Transactional
    public void ungroup(TableGroupIdRequestDto requestDto) {
        TableGroup tableGroup = tableGroupRepository.findById(requestDto.getId())
            .orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = tableRepository
            .findAllByTableGroupId(requestDto.getId());

        if (ordersRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables,
            Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        tableGroup.remove(orderTables);

        entityManager.flush();
    }
}
