package kitchenpos.integration;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    public void 테이블_그룹_등록() {
        //given
        final OrderTable orderTable1 = tableApi.테이블_등록(2, true).getContent();
        final OrderTable orderTable2 = tableApi.테이블_등록(4, true).getContent();

        //when
        final MockMvcResponse<TableGroup> result =
            tableGroupApi.테이블_그룹_등록(orderTable1,orderTable2);

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(result.getContent()).isNotNull();
        Assertions.assertThat(result.getContent().getOrderTables()).hasSize(2);
    }

    @Test
    public void 테이블_그룹_목록_삭제() {
        //given
        final OrderTable orderTable1 = tableApi.테이블_등록(2, true).getContent();
        final OrderTable orderTable2 = tableApi.테이블_등록(4, true).getContent();
        final TableGroup insertTableGroup =
            tableGroupApi.테이블_그룹_등록(orderTable1,orderTable2).getContent();

        //when
        final MockMvcResponse<Void> result = tableGroupApi.테이블_그룹_목록_삭제(insertTableGroup.getId());

        //then
        Assertions.assertThat(result.getHttpStatus()).isEqualTo(HttpStatus.NO_CONTENT);

        final TableGroup foundTableGroup = tableGroupDao.findById(insertTableGroup.getId()).get();
        Assertions.assertThat(foundTableGroup.getOrderTables()).isNull();
    }
}
