package igor.shmidt.router.routing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import igor.shmidt.router.routing.service.RouterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WikiController.class)
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RouterService routerService;

    @Test
    void test() throws Exception {
        Map<String, Object> reqBody = Map.of("start", "Albert Einstein", "end", "GitHub");
        String bodyVal = "Albert Einstein -> Bill Gates -> GitHub";
        String resBody = objectMapper.writer().writeValueAsString(reqBody);
        when(routerService.sendToWorker(anyMap()))
                .thenReturn(bodyVal);

        mockMvc.perform(post("/wiki/way").contentType(MediaType.APPLICATION_JSON).content(resBody))
                .andExpect(status().isOk())
                .andExpect(content().string(bodyVal));
    }

}