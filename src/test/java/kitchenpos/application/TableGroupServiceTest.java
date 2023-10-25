package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest implements ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블_그룹의_생성_시_존재하지_않는_주문_테이블이_있다면_예외가_발생한다() {
        // given
        final List<Long> wrongOrderTableIds = List.of(Long.MAX_VALUE, Long.MAX_VALUE);

        // expected
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(wrongOrderTableIds)))
                .isInstanceOf(TableGroupException.NoOrderTableException.class);
    }

    @Test
    void 존재하지_않는_그룹_지정을_그룹_해제할_수_없다() {
        // given
        final Long wrongTableGroupId = Long.MAX_VALUE;

        // expected
        assertThatThrownBy(() -> tableGroupService.ungroup(wrongTableGroupId))
                .isInstanceOf(TableGroupException.NotFoundException.class);
    }
}
