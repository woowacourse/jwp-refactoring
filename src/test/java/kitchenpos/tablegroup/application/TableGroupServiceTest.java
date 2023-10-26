package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.exception.KitchenPosException;
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
import org.springframework.context.ApplicationEventPublisher;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

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
        void 성공() {
            // given
            var request = new TableGroupCreateRequest(List.of(1L, 2L));
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(new TableGroup(1L, createdDate));

            // when
            var actual = tableGroupService.create(request);

            // then
            assertThat(actual.getOrderTableIds()).hasSize(2);
        }
    }

    @Nested
    class ungroup {

        @Test
        void 성공() {
            // when
            tableGroupService.ungroup(1L);

            // then
            verify(tableGroupRepository, atLeastOnce()).deleteById(anyLong());
        }
    }
}
