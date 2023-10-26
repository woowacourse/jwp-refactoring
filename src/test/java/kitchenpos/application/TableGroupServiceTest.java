package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixtures.Fixtures;
import kitchenpos.ui.dto.request.TableGroupRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    Fixtures fixtures;

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    JpaOrderTableRepository jpaOrderTableRepository;

    @Nested
    class 단체_지정_등록 {

        @Test
        void 단체_지정을_등록한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();
            OrderTable orderTableB = fixtures.빈_테이블_저장();

            TableGroupRequest request = new TableGroupRequest(
                    List.of(orderTableA.getId(), orderTableB.getId())
            );

            // when
            TableGroupResponse result = tableGroupService.create(request);

            // then
            List<Long> orderTableIds = result.getOrderTableResponses()
                    .stream()
                    .map(OrderTableResponse::getId)
                    .collect(Collectors.toList());

            assertThat(orderTableIds).contains(orderTableA.getId(), orderTableB.getId());
        }

        @Test
        void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
            // given
            OrderTable orderTableA = fixtures.빈_테이블_저장();

            TableGroupRequest request = new TableGroupRequest(
                    List.of(orderTableA.getId(), -1L)
            );

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class 단체_지정_헤제 {
        @Test
        void 단체_지정을_헤제한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            OrderTable orderTableA = fixtures.주문_테이블_저장(tableGroup, false);
            OrderTable orderTableB = fixtures.주문_테이블_저장(tableGroup, false);
            fixtures.주문_저장(orderTableA, OrderStatus.COMPLETION);
            fixtures.주문_저장(orderTableB, OrderStatus.COMPLETION);
            ReflectionTestUtils.setField(orderTableA, "isEmpty", true);
            ReflectionTestUtils.setField(orderTableB, "isEmpty", true);
            entityManager.clear();

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            List<OrderTable> orderTables = jpaOrderTableRepository.findAllByIdIn(
                    List.of(orderTableA.getId(), orderTableB.getId())
            );
            assertThat(orderTables.get(0).getTableGroup()).isNull();
            assertThat(orderTables.get(1).getTableGroup()).isNull();
        }

        @Test
        void 주문_테이블_상태가_계산완료가_아닌_경우_예외가_발생한다() {
            // given
            TableGroup tableGroup = fixtures.단체_지정_저장();
            OrderTable orderTableA = fixtures.주문_테이블_저장(tableGroup, false);
            OrderTable orderTableB = fixtures.주문_테이블_저장(tableGroup, false);
            fixtures.주문_저장(orderTableA, OrderStatus.COOKING);
            fixtures.주문_저장(orderTableB, OrderStatus.COMPLETION);
            ReflectionTestUtils.setField(orderTableA, "isEmpty", true);
            ReflectionTestUtils.setField(orderTableB, "isEmpty", true);
            entityManager.clear();

            // when
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
