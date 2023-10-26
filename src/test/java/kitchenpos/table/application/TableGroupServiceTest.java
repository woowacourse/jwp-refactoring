package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.table.domain.model.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.request.TableGroupTableRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.supports.OrderTableFixture;
import kitchenpos.table.supports.TableGroupFixture;
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

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @Nested
    class 단체_지정 {

        @Test
        void 모든_주문_테이블은_DB에_존재해야한다() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new TableGroupTableRequest(1L),
                new TableGroupTableRequest(2L)
            ));

            given(orderTableRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(List.of(
                    OrderTableFixture.fixture().id(1L).build()
                ));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 단체_지정_성공() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                new TableGroupTableRequest(1L),
                new TableGroupTableRequest(2L)
            ));

            given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(TableGroupFixture.fixture().id(1L).build());
            given(orderTableRepository.findAllById(eq(List.of(1L, 2L))))
                .willReturn(List.of(
                    OrderTableFixture.fixture().id(1L).empty(true).tableGroup(null).build(),
                    OrderTableFixture.fixture().id(2L).empty(true).tableGroup(null).build()
                ));

            // when
            TableGroupResponse response = tableGroupService.create(request);
            System.out.println("response = " + response);

            // then
            assertThat(response.getId()).isEqualTo(1L);
        }
    }
}
