package vn.edu.uet.cinema.api.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.uet.cinema.TestcontainersConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class AuditLogApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void adminMutationsRecordAuditLogsAndSupportPagination() throws Exception {
        String admin = token("admin@cinema.local", "admin12345");

        String created = mvc.perform(post("/api/movies").header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON).content(movieJson("Audit Test Movie")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String movieId = JsonPath.read(created, "$.id");

        mvc.perform(put("/api/movies/" + movieId).header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON).content(movieJson("Audit Test Movie Updated")))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/movies/" + movieId).header("Authorization", "Bearer " + admin))
                .andExpect(status().isNoContent());

        String firstPage = mvc.perform(get("/api/admin/audit-logs?page=1&size=2")
                        .header("Authorization", "Bearer " + admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalItems").value(greaterThanOrEqualTo(3)))
                .andReturn().getResponse().getContentAsString();

        List<String> actions = JsonPath.read(firstPage, "$.items[*].action");
        List<String> targetIds = JsonPath.read(firstPage, "$.items[*].targetId");
        assertThat(actions).containsExactly("DELETE_MOVIE", "UPDATE_MOVIE");
        assertThat(targetIds).containsExactly(movieId, movieId);

        String secondPage = mvc.perform(get("/api/admin/audit-logs?page=2&size=2")
                        .header("Authorization", "Bearer " + admin))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<String> page2Actions = JsonPath.read(secondPage, "$.items[*].action");
        assertThat(page2Actions).contains("CREATE_MOVIE");
    }

    @Test
    void auditLogsEndpointRequiresAdminRole() throws Exception {
        mvc.perform(get("/api/admin/audit-logs"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        register("audit-customer@example.com");
        String customer = token("audit-customer@example.com", "password123");

        mvc.perform(get("/api/admin/audit-logs").header("Authorization", "Bearer " + customer))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
    }

    private String token(String email, String password) throws Exception {
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.accessToken");
    }

    private void register(String email) throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"%s\",\"password\":\"password123\",\"fullName\":\"Audit Customer\"}".formatted(email)))
                .andExpect(status().isCreated());
    }

    private static String movieJson(String title) {
        return ("{\"title\":\"%s\",\"description\":\"Audit description\",\"durationMinutes\":120,"
                + "\"genre\":\"Drama\",\"ageRating\":\"P\",\"releaseDate\":\"2026-01-15\","
                + "\"posterUrl\":\"https://example.com/audit.jpg\"}").formatted(title);
    }
}
