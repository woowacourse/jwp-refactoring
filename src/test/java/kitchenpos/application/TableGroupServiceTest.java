package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.dto.CreateTableGroupOrderTableRequest;
import kitchenpos.dto.CreateTableGroupRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @PersistenceContext
    private EntityManager em;

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

        em.flush();
        em.clear();

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블.getId());
        final CreateTableGroupRequest tableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 네명_테이블_아이디));

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 목록이 비어있으면 예외가 발생한다")
    void create_emptyOrderTables() {
        // given
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블 목록이 1개이면 예외가 발생한다")
    void create_oneOrderTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        em.flush();
        em.clear();

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하려는 테이블은 2개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 등록하려는 테이블이 모두 존재하지 않으면 예외가 발생한다")
    void create_invalidNumberOfTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));

        em.flush();
        em.clear();

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 존재하지_않는_테이블_아이디 = new CreateTableGroupOrderTableRequest(10L);
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 존재하지_않는_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화를 요청한 테이블 중에 존재하지 않는 테이블이 포함되어 있습니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블이 비어있지 않으면 예외가 발생한다")
    void create_notEmptyTable() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable 네명_테이블_사용중 = orderTableRepository.save(new OrderTable(4, false));

        em.flush();
        em.clear();

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 사용중인_네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블_사용중.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 사용중인_네명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 그룹화하려면 테이블이 비어있고 그룹화되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 등록할 때 테이블이 이미 그룹화 되어 있다면 예외가 발생한다")
    void create_alreadyGroup() {
        // given
        final OrderTable 두명_테이블 = orderTableRepository.save(new OrderTable(2, true));
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup());
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        em.flush();
        em.clear();

        final CreateTableGroupOrderTableRequest 두명_테이블_아이디 = new CreateTableGroupOrderTableRequest(두명_테이블.getId());
        final CreateTableGroupOrderTableRequest 그룹화된_네명_테이블_아이디 = new CreateTableGroupOrderTableRequest(네명_테이블.getId());
        final CreateTableGroupRequest invalidTableGroup = new CreateTableGroupRequest(List.of(두명_테이블_아이디, 그룹화된_네명_테이블_아이디));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블을 그룹화하려면 테이블이 비어있고 그룹화되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다")
    void ungroup() {
        // given
        final OrderTable 세명_테이블 = orderTableRepository.save(new OrderTable(3, true));
        final OrderTable 네명_테이블 = orderTableRepository.save(new OrderTable(4, true));
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup());
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        em.flush();
        em.clear();

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
        final TableGroup 그룹화된_세명_네명_테이블 = tableGroupRepository.save(new TableGroup());
        그룹화된_세명_네명_테이블.initOrderTables(List.of(세명_테이블, 네명_테이블));

        orderRepository.save(new Order(세명_테이블, orderStatus));
        orderRepository.save(new Order(네명_테이블, orderStatus));

        em.flush();
        em.clear();

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(그룹화된_세명_네명_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹을 해제하려면 그룹화된 테이블의 모든 주문이 완료 상태이어야 합니다.");
    }
}
