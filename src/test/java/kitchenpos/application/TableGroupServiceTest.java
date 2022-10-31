package kitchenpos.application;

import static kitchenpos.fixture.domain.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.domain.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableCreateDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create_success() {
        // given
        TableGroup tableGroup = createTableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable1 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(orderTable1.getId()), new TableCreateDto(orderTable2.getId())));

        // when
        TableGroup savedTableGroup = tableGroupService.create(request);

        // then
        TableGroup dbTableGroup = tableGroupRepository.findById(savedTableGroup.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbTableGroup.getId()).isEqualTo(savedTableGroup.getId());
    }

    @DisplayName("단체 지정을 생성할 때 빈 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_emptyOrderTable() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 생성할 때 주문테이블의 개수가 하나라면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_is_one() {
        // given
        TableGroup tableGroup = createTableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Collections.singletonList(new TableCreateDto(orderTable.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 생성할 때 개별 주문테이블이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_not_exist() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(999999L), new TableCreateDto(999999L)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 생성할 때 개별 주문테이블이 비어있지 않으면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_not_empty() {
        // given
        TableGroup tableGroup = createTableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable1 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, false));
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(orderTable1.getId()), new TableCreateDto(orderTable2.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 생성할 때 개별 주문테이블이 이미 단체지정이 되어있다면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_has_orderTableGroup() {
        // given
        TableGroup tableGroup = createTableGroup(LocalDateTime.now(), new ArrayList<>());
        OrderTable orderTable1 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(orderTable1.getId()), new TableCreateDto(orderTable2.getId())));
        tableGroupService.create(createRequest);

        OrderTable orderTable3 = orderTableRepository.save(createOrderTable(4, true));
        TableGroupCreateRequest request = new TableGroupCreateRequest(
                Arrays.asList(new TableCreateDto(orderTable1.getId()), new TableCreateDto(orderTable3.getId())));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup_success() {
        // given
        TableGroup tableGroup = tableGroupRepository.save(createTableGroup(LocalDateTime.now(), new ArrayList<>()));
        OrderTable orderTable1 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(tableGroup.getId(), 4, true));
        TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup(
                LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2)));

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(orderTables).isEmpty();
    }
}
