package kitchenpos.ui.table;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.dto.table.response.TableGroupResponse;
import kitchenpos.exception.badrequest.AlreadyGroupedException;
import kitchenpos.exception.badrequest.CookingOrMealOrderTableCannotUngroupedException;
import kitchenpos.exception.badrequest.InvalidOrderTableSizeException;
import kitchenpos.exception.badrequest.OrderTableNotEmptyException;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import kitchenpos.exception.badrequest.TableGroupNotExistsException;
import kitchenpos.ui.product.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableGroupRestControllerTest extends RestControllerTest {

    @Test
    void 단체_지정에_성공한다() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);
        OrderTableResponse orderTable1 = new OrderTableResponse(1L, null, 1, false);
        OrderTableResponse orderTable2 = new OrderTableResponse(1L, null, 1, false);
        TableGroupResponse expected =
                new TableGroupResponse(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2));

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }

    @Test
    void 단체_지정하려는_테이블이_2개_미만이면_400() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenThrow(
                new InvalidOrderTableSizeException());

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 단체_지정하려는_테이블이_존재하지_않으면_400() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenThrow(
                new OrderTableNotExistsException());

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 단체_지정하려는_테이블이_빈_테이블이_아니면_400() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenThrow(new OrderTableNotEmptyException());

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 단체_지정하려는_테이블이_이미_단체_지정되어_있으면_400() throws Exception {
        TableGroupCreateRequest request = new TableGroupCreateRequest(1L, 2L);

        when(tableGroupService.create(any(TableGroupCreateRequest.class))).thenThrow(new AlreadyGroupedException());

        mockMvc.perform(post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 단체_지정_해제에_성공한다() throws Exception {
        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void 단체_지정_해제하려는_테이블이_존재하지_않을_경우_400() throws Exception {
        doThrow(new TableGroupNotExistsException()).when(tableGroupService).ungroup(1L);

        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 단체_지정_해제하려는_테이블에_조리_또는_식사_중인_주문이_있을_경우_400() throws Exception {
        doThrow(new CookingOrMealOrderTableCannotUngroupedException()).when(tableGroupService).ungroup(1L);

        mockMvc.perform(delete("/api/table-groups/1"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
