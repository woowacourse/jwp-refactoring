package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.CreateTableGroupOrderTableRequest;
import kitchenpos.ui.dto.CreateTableGroupRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("테이블 그룹을 등록한다")
    void create() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블.getId());
        final CreateTableGroupRequest tableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 네명_테이블_아이디));

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getCreatedDate()).isNotNull();
        });
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 목록이 비어있으면 예외가 발생한다")
    void create_emptyOrderTables() {
        // given
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 목록이 1개이면 예외가 발생한다")
    void create_oneOrderTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 등록하려는 테이블이 모두 존재하지 않으면 예외가 발생한다")
    void create_invalidNumberOfTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 존재하지_않는_테이블_아이디 = new CreateTableGroupOrderTableRequest(10L);

        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 존재하지_않는_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블이 비어있지 않으면 예외가 발생한다")
    void create_notEmptyTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable 네명_테이블_사용중 = orderTableRepository.save(new OrderTable(4, false));

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 사용중인_네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블_사용중.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 사용중인_네명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블이 이미 그룹화 되어 있다면 예외가 발생한다")
    void create_alreadyGroup() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 그룹화된_네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 그룹화된_네명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        // when
        tableGroupService.ungroup(그룹화된_세명_네명_테이블.getId());

        // then
        final List<OrderTable> actual = orderTableRepository.findAllByIdIn(List.of(세명_테이블.getId(), 네명_테이블.getId()));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getTableGroup()).isNull();
            softAssertions.assertThat(actual.get(0).isEmpty()).isFalse();
            softAssertions.assertThat(actual.get(1).getTableGroup()).isNull();
            softAssertions.assertThat(actual.get(1).isEmpty()).isFalse();
        });
    }

    @ParameterizedTest(name = "주문 상태가 {0}일 때")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("테이블 그룹을 해제할 때 해제하려는 테이블의 주문이 조리중이나 식사중이면 예외가 발생한다")
    void ungroup_invalidOrderStatus(final OrderStatus orderStatus) {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        orderRepository.save(new Order(세명_테이블, orderStatus, LocalDateTime.now()));
        orderRepository.save(new Order(네명_테이블, orderStatus, LocalDateTime.now()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(그룹화된_세명_네명_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
