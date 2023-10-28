package kitchenpos.table.application;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import kitchenpos.ServiceIntegrationTest;
import kitchenpos.fixture.TableFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableChangeEmptyValidator;
import kitchenpos.table.domain.listener.TableGroupEventListener;
import kitchenpos.table_group.application.TableGroupCreateEvent;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TableGroupEventListenerTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupEventListener tableGroupEventListener;

    @Autowired
    private OrderTableRepository tableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @MockBean
    private TableChangeEmptyValidator tableChangeEmptyValidator;

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("tableGroupId를 table에 세팅해준다.")
    void groupOrderTables() {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final Long tableGroupId = tableGroupRepository.save(tableGroup).getId();
        final Long savedOrderTableId1 = tableService.create(TableFixture.비어있는_주문_테이블_DTO()).getId();
        final Long savedOrderTableId2 = tableService.create(TableFixture.비어있는_주문_테이블_DTO()).getId();

        final List<Long> savedOrderTableIds = List.of(savedOrderTableId1, savedOrderTableId2);

        final TableGroupCreateEvent event
            = new TableGroupCreateEvent(tableGroupId, savedOrderTableIds);

        //when
        tableGroupEventListener.groupOrderTables(event);

        //then
        final Set<Long> orderTableIds = tableRepository.findAllByIdIn(savedOrderTableIds)
            .stream()
            .map(OrderTable::getTableGroupId)
            .collect(toUnmodifiableSet());

        Assertions.assertThat(orderTableIds)
            .containsExactly(tableGroupId);
    }
}
