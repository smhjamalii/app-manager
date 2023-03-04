package ir.welldone.appmanager.view.rest;

import ir.welldone.appmanager.view.dto.AppDTO;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class AppResourceTest {

    @Value("${api-key}")
    private String apiKey;

    @Test
    public void shouldCallCreateEndpointButFail(){

        AppDTO appDTO = AppDTO.builder()
                .name("Invalid app")
                .build();

        WebTestClient testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        FluxExchangeResult<String> fluxExchangeResult =
                testClient.post().uri("/apps")
                .body(Mono.just(appDTO), AppDTO.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(String.class);

        log.debug("Endpoint call Failed with status: {}", fluxExchangeResult.getStatus().value());
    }

    @Test
    public void shouldCallCreateEndpointAndSuccess(){

        AppDTO appDTO = AppDTO.builder()
                .name("EDL")
                .desc("This is every day learning app")
                .domain("https://edl.org.gov")
                .enabled(Boolean.TRUE)
                .build();

        WebTestClient testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        FluxExchangeResult<String> fluxExchangeResult =
                testClient.post().uri("/apps")
                        .body(Mono.just(appDTO), AppDTO.class)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header("api-key", apiKey)
                        .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                        .acceptCharset(StandardCharsets.UTF_8)
                        .ifNoneMatch("*")
                        .ifModifiedSince(ZonedDateTime.now())
                        .exchange()
                        .expectStatus().is2xxSuccessful()
                        .returnResult(String.class);

        log.debug("Endpoint call success: {}, {}", fluxExchangeResult.getStatus().value(), fluxExchangeResult.getResponseBody());
    }

    @Test
    public void shouldFindAnApp(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        EntityExchangeResult<AppDTO> result =
            testClient.get().uri("/apps/2")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("api-key", apiKey)
                    .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .exchange()
                    .expectStatus().is2xxSuccessful()
                    .expectBody(AppDTO.class)
                    .returnResult();

        AppDTO appDTO = result.getResponseBody();
        log.debug("App {} loaded", appDTO.getName());
    }

    @Test
    public void shouldNotFindAnApp(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        EntityExchangeResult<String> result =
                testClient.get().uri("/apps/20")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header("api-key", apiKey)
                        .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                        .acceptCharset(StandardCharsets.UTF_8)
                        .exchange()
                        .expectStatus().is4xxClientError()
                        .expectBody(String.class)
                        .returnResult();

        log.debug(result.getResponseBody());
    }

    @Test
    public void shouldSearchVeryCarefully(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        EntityExchangeResult<List<AppDTO>> searchResult = testClient.get().uri("/apps?name=Bird&active=true&start=0&count=5")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(AppDTO.class)
                .returnResult();

        Assert.notNull(searchResult, "Result must not be null");
    }

    @Test
    public void shouldSearchWithNoResult(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        FluxExchangeResult<String> searchResult = testClient.get().uri("/apps?name=gym&active=true&start=0&count=5")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().is4xxClientError()
                .returnResult(String.class);

        Assert.isTrue(searchResult.getStatus().is4xxClientError(), "Status code should be 404");
    }

    @Test
    public void shouldUpdateAnApp(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        EntityExchangeResult<AppDTO> loadedApp = testClient.get().uri("/apps/2")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                    .expectBody(AppDTO.class)
                    .returnResult();

        AppDTO appDTO = loadedApp.getResponseBody();
        log.debug("App {} loaded and is ready to update", appDTO.getName());

        appDTO.setName("Gymnastic");

        testClient.put().uri("/apps")
                .body(Mono.just(appDTO), AppDTO.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .exchange()
                .expectStatus().is2xxSuccessful();

        loadedApp = testClient.get().uri("/apps/2")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectBody(AppDTO.class)
                .returnResult();

        Assert.isTrue(loadedApp.getResponseBody().getName().equals("Gymnastic"), "Name property of the app should be updated");
    }

    @Test
    public void shouldDeleteAnApp(){
        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .build();

        EntityExchangeResult<AppDTO> loadedApp = testClient.get().uri("/apps/4")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(AppDTO.class)
                .returnResult();

        testClient.delete().uri("/apps/4")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().is2xxSuccessful();

        EntityExchangeResult<String> loadResult = testClient.get().uri("/apps/4")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("api-key", apiKey)
                .accept(AppManagerMediaTypes.APPLICATION_APP_MANAGER_V1)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .returnResult();

        Assert.isTrue(loadResult.getStatus().is4xxClientError(), "Should return a 404 code for calling findById after calling delete");
    }

}
