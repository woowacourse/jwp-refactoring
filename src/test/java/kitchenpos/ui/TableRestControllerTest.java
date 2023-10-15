//package kitchenpos.ui;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.List;
//import kitchenpos.application.TableService;
//import kitchenpos.domain.OrderTable;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(TableRestController.class)
//class TableRestControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TableService tableService;
//
//    @Test
//    void create() throws Exception {
//        // given
//        final OrderTable result = new OrderTable();
//        result.setId(1L);
//        given(tableService.create(any())).willReturn(result);
//
//        final OrderTable request = new OrderTable();
//        request.setEmpty(false);
//        request.setNumberOfGuests(4);
//
//        // when
//        mockMvc.perform(post("/api/tables")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().string("location", "/api/tables/1"));
//    }
//
//    @Test
//    void list() throws Exception {
//        // given
//        final OrderTable resultA = new OrderTable();
//        resultA.setEmpty(false);
//        resultA.setNumberOfGuests(4);
//        final OrderTable resultB = new OrderTable();
//        resultB.setEmpty(true);
//        resultB.setNumberOfGuests(2);
//        given(tableService.list()).willReturn(List.of(resultA, resultB));
//
//        // when
//        mockMvc.perform(get("/api/tables"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(List.of(resultA, resultB))));
//    }
//
//    @Test
//    void changeEmpty() throws Exception {
//        // given
//        final OrderTable result = new OrderTable();
//        result.setEmpty(false);
//        result.setNumberOfGuests(4);
//        given(tableService.changeEmpty(any(), any())).willReturn(result);
//
//        final OrderTable request = new OrderTable();
//        request.setEmpty(true);
//        request.setNumberOfGuests(4);
//
//        // when
//        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(result)));
//    }
//
//    @Test
//    void changeNumberOfGuests() throws Exception {
//        // given
//        final OrderTable result = new OrderTable();
//        result.setEmpty(false);
//        result.setNumberOfGuests(4);
//        given(tableService.changeNumberOfGuests(any(), any())).willReturn(result);
//
//        final OrderTable request = new OrderTable();
//        request.setEmpty(false);
//        request.setNumberOfGuests(1);
//
//        // when
//        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(objectMapper.writeValueAsString(result)));
//    }
//}
