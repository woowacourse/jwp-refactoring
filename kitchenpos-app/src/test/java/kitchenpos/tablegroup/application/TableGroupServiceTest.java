package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableInTableGroupDto;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Nested
    class create_성공_테스트 {

        @Test
        void 단체_주문을_생성할_수_있다() {
            // given
            orderTableRepository.save(new OrderTable(null, 3, false));
            orderTableRepository.save(new OrderTable(null, 2, false));

            final var orderTableRequest1 = new OrderTableInTableGroupDto(1L);
            final var orderTableRequest2 = new OrderTableInTableGroupDto(2L);
            final var request = new TableGroupCreateRequest(List.of(orderTableRequest1, orderTableRequest2));

            // when
            final var actual = tableGroupService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {
        @Test
        void 단체_주문_테이블의_수와_디비에_저장된_테이블의_수가_다른_경우_예외를_반환한다() {
            // given
            orderTableRepository.save(new OrderTable(null, 3, true));
            orderTableRepository.save(new OrderTable(null, 2, true));

            final var orderTableRequest1 = new OrderTableInTableGroupDto(1L);
            final var orderTableRequest2 = new OrderTableInTableGroupDto(2L);
            final var orderTableRequest3 = new OrderTableInTableGroupDto(3L);
            final var request = new TableGroupCreateRequest(
                    List.of(orderTableRequest1, orderTableRequest2, orderTableRequest3));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }
    }

    @Nested
    class ungroup_성공_테스트 {

        @Test
        void 모든_테이블_주문이_완료할_수_있다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            orderTableRepository.save(new OrderTable(1L, 3, false));
            orderTableRepository.save(new OrderTable(1L, 4, false));

            // when
            tableGroupService.ungroup(1L);
            final var actual1 = orderTableRepository.findById(1L).get();
            final var actual2 = orderTableRepository.findById(2L).get();

            // then
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(actual1.getTableGroupId()).isNull();
                soft.assertThat(actual1.isEmpty()).isFalse();
                soft.assertThat(actual2.getTableGroupId()).isNull();
                soft.assertThat(actual2.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class ungroup_실패_테스트 {

        @Test
        void 아직_완료되지_않은_테이블_주문이_존재하면_에러를_반환한다() {
            // given
            tableGroupRepository.save(TableGroup.create());
            final var orderTable = orderTableRepository.save(new OrderTable(1L, 5, false));
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));
            menuRepository.save(new Menu("메뉴_이름", BigDecimal.valueOf(0), menuGroup, Collections.emptyList()));
            final var orderLineItem = new OrderLineItem(1L, "메뉴_이름", BigDecimal.valueOf(0), 5L);
            orderRepository.save(new Order(orderTable, OrderStatus.MEAL, List.of(orderLineItem)));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}
