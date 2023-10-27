package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.domain.service.TableGroupValidator;
import kitchenpos.table.domain.service.TableUngroupValidator;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.request.TableGroupTableRequest;
import kitchenpos.table.supports.OrderTableFixture;
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

    @Mock
    TableGroupValidator tableGroupValidator;

    @Mock
    TableUngroupValidator tableUngroupValidator;

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
            assertThatThrownBy(() -> tableGroupService.group(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }
    }
}
