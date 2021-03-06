package fr.deroffal.bibliotheque.webapp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * https://blog.codefx.org/java/http-2-api-tutorial/
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HttpService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    private static boolean isSuccess(final int statusCode) {
        return statusCode < SC_BAD_REQUEST;
    }

    public <T> T get(final String uri, final Map<String, String> headers, final Class<T> clazz, final Supplier<T> failure) {
        return get(
                uri,
                headers,
                it -> objectMapper.readValue(it, clazz),
                failure
        );
    }

    public <T> T get(final String uri, final Class<T> clazz, final Supplier<T> failure) {
        return get(
                uri,
                new HashMap<>(),
                it -> objectMapper.readValue(it, clazz),
                failure
        );
    }

    public <T> T post(final String uri, final Object body, final Class<T> clazz, final Supplier<T> failure) {
        return post(
                uri,
                body,
                it -> objectMapper.readValue(it, clazz),
                failure
        );
    }

    public <T> T get(final String uri, final Map<String, String> headers, final CheckedFunction<String, T> success, final Supplier<T> failure) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(uri)).GET();
        headers.forEach(builder::header);
        HttpRequest request = builder.build();
        try {
            final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                return success.apply(response.body());
            }
        } catch (final Exception e) {
            log.warn("GET {} - exception", uri, e);
        }
        return failure.get();
    }

    public <T> T post(final String uri, final Object body, final CheckedFunction<String, T> success, final Supplier<T> failure) {
        try {
            String requestBody = objectMapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            final HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (isSuccess(response.statusCode())) {
                return success.apply(response.body());
            }
        } catch (final Exception e) {
            log.warn("POST {} - exception", uri, e);
        }
        return failure.get();
    }
}
