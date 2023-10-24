package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @Nested
    class create {

        @Test
        void 주문_테이블_목록이_비어있으면_예외() {
            // given
            var request = new TableGroupCreateRequest(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("주문 테이블 목록은 2개 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블_목록이_1개_이하면_예외() {
            // given
            var request = new TableGroupCreateRequest(List.of(1L));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("주문 테이블 목록은 2개 이상이어야 합니다.");
        }

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            var request = new TableGroupCreateRequest(List.of(1L, 2L, 4885L, 4886L));
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(new TableGroup(1L, createdDate));
            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    new OrderTable(1L, false, 0),
                    new OrderTable(2L, false, 0)
                ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("존재하지 않는 주문 테이블이 있습니다. notExistOrderTableIds=[4885, 4886]");
        }

        @Test
        void 성공() {
            // given
            var request = new TableGroupCreateRequest(List.of(1L, 2L));
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(new TableGroup(1L, createdDate));
            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    new OrderTable(1L, false, 0),
                    new OrderTable(2L, false, 0)
                ));

            // when
            var actual = tableGroupService.create(request);

            // then
            assertThat(actual.getOrderTableIds()).hasSize(2);
        }
    }

    @Nested
    class ungroup {

        @Test
        void 계산_완료_상태가_아닌_주문이_있으면_예외() {
            // given
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            given(orderRepository.findAllByOrderTableIdIn(anyList()))
                .willReturn(List.of(
                    new Order(1L, OrderStatus.COMPLETION, orderedTime, new OrderTable(1L, false, 0)),
                    new Order(2L, OrderStatus.COMPLETION, orderedTime, new OrderTable(1L, false, 0)),
                    new Order(3L, OrderStatus.MEAL, orderedTime, new OrderTable(1L, false, 0))
                ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("계산 완료 상태가 아닌 주문이 있는 테이블은 그룹을 해제할 수 없습니다.");
        }

        @Test
        void 성공() {
            // given
            LocalDateTime orderedTime = LocalDateTime.parse("2023-10-15T22:40:00");
            given(orderRepository.findAllByOrderTableIdIn(anyList()))
                .willReturn(List.of(
                    new Order(1L, OrderStatus.COMPLETION, orderedTime, new OrderTable(1L, false, 0)),
                    new Order(2L, OrderStatus.COMPLETION, orderedTime, new OrderTable(1L, false, 0)),
                    new Order(3L, OrderStatus.COMPLETION, orderedTime, new OrderTable(1L, false, 0))
                ));

            // when & then
            assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(1L));
        }
    }
}
