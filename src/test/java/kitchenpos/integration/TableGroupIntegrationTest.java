package kitchenpos.integration;

import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.integration.api.TableApi;
import kitchenpos.integration.api.TableGroupApi;
import kitchenpos.integration.utils.MockMvcResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class TableGroupIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupApi tableGroupApi;
    @Autowired
    private TableApi tableApi;

    @Test
    public void 테이블_그룹_등록() {
        //given
        final OrderTableResponse orderTable1 = tableApi.테이블_등록(2, true).getContent();
        final OrderTableResponse orderTable2 = tableApi.테이블_등록(4, true).getContent();

        //when
        final MockMvcResponse<TableGroupResponse> result =
            tableGroupApi.테이블_그룹_등록(orderTable1.getId(),orderTable2.getId());

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(result.getContent()).isNotNull();
        Assertions.assertThat(result.getContent().getOrderTables()).hasSize(2);
    }

    @Test
    public void 테이블_그룹_목록_삭제() {
        //given
        final OrderTableResponse orderTable1 = tableApi.테이블_등록(2, true).getContent();
        final OrderTableResponse orderTable2 = tableApi.테이블_등록(4, true).getContent();
        final TableGroupResponse insertTableGroup =
            tableGroupApi.테이블_그룹_등록(orderTable1.getId(),orderTable2.getId()).getContent();

        //when
        final MockMvcResponse<Void> result = tableGroupApi.테이블_그룹_목록_삭제(insertTableGroup.getId());

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
