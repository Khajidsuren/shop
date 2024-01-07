package mn.shop.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private static final int defaultTimeOut = 15;
    private static final int defaultMaxIdleConnections = 2;
    private static final Long defaultKeepIdleDuration = 15L;
    private static final TimeUnit measure;
    private static final String encoding = "UTF-8";
    private static final String delimiter = "&";
    private static final String equalizer = "=";
    private static final String delimiterF = "?";

    static {
        measure = TimeUnit.SECONDS;
    }

    private OkHttpClient client;
    private ObjectMapper mapper;
    private String host;
    private Boolean debugResponse = false;
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    public HttpClient(String host) {
        this.host = host;
        this.initRequest();
    }

    public HttpClient(String host, int timeout, TimeUnit measure) {
        this.host = host;
        this.initRequest(timeout, measure);
    }

    public HttpClient(String host, int timeout, TimeUnit measure, ConnectionPool connectionPool) {
        this.host = host;
        this.initRequest(timeout, measure, connectionPool);
    }

    public HttpClient(final OkHttpClient client, final ObjectMapper mapper, final String host, final Boolean debugResponse, final MediaType mediaType) {
        this.client = client;
        this.mapper = mapper;
        this.host = host;
        this.debugResponse = debugResponse;
        this.mediaType = mediaType;
    }

    public static HttpClientBuilder builder() {
        return new HttpClientBuilder();
    }

    public OkHttpClient getInstance() {
        return this.client;
    }

    private void initMapper() {
        this.mapper = new ObjectMapper();
    }

    private void initRequest() {
        this.initMapper();
        this.client = (new OkHttpClient.Builder()).readTimeout(15L, measure).connectionPool(new ConnectionPool(2, defaultKeepIdleDuration, TimeUnit.SECONDS)).build();
    }

    private void initRequest(int timeout, TimeUnit measure) {
        this.initMapper();
        this.client = (new OkHttpClient.Builder()).readTimeout((long) timeout, measure).connectionPool(new ConnectionPool(2, defaultKeepIdleDuration, TimeUnit.SECONDS)).build();
    }

    private void initRequest(int timeout, TimeUnit measure, ConnectionPool connectionPool) {
        this.initMapper();
        this.client = (new OkHttpClient.Builder()).readTimeout((long) timeout, measure).connectionPool(connectionPool).build();
    }

    public RequestBody body(String body) {
        return RequestBody.create(this.mediaType, body);
    }

    public Headers buildHeaders(String key, String value) {
        return (new Headers.Builder()).add(key, value).build();
    }

    public Headers buildHeaders(Map<String, String> headers) {
        return Headers.of(headers);
    }

    public Headers buildHeaders(String... namesAndValues) {
        return Headers.of(namesAndValues);
    }

    public Response post(String url, Object payload, Headers headers) throws IOException {
        return this.processRequest(url, payload, headers, RequestMethod.POST);
    }

    public Response post(String url, RequestBody body, Headers headers) throws IOException {
        return this.processRequest(url, body, headers, RequestMethod.POST);
    }

    public Response post(Object payload, Headers headers) throws IOException {
        return this.processRequest(this.host, payload, headers, RequestMethod.POST);
    }

    public Response post(String json, Headers headers) throws IOException {
        return this.request(this.host, this.body(json), headers, RequestMethod.POST);
    }

    public Response asyncPost(String json, Headers headers, Callback callback) throws IOException {
        return this.request(this.host, this.body(json), headers, RequestMethod.POST, callback);
    }

    public Response get(String url, Object payload, Headers headers) throws IOException {
        return this.processRequest(url, payload, headers, RequestMethod.GET);
    }

    public Response get(Object payload, Headers headers) throws IOException {
        return this.processRequest(this.host, payload, headers, RequestMethod.GET);
    }

    public Response get(String url, Headers headers) throws IOException {
        return this.processRequest(url, (RequestBody) null, headers, RequestMethod.GET);
    }

    public Response get(Headers headers) throws IOException {
        return this.processRequest(this.host, (RequestBody) null, headers, RequestMethod.GET);
    }

    public Response get(String url, Object payload, Headers headers, Map<String, Object> queryParams) throws IOException {
        return this.processRequest(url + this.processQueryParams(queryParams), payload, headers, RequestMethod.GET);
    }

    public Response get(Object payload, Headers headers, Map<String, Object> queryParams) throws IOException {
        return this.processRequest(this.host + this.processQueryParams(queryParams), payload, headers, RequestMethod.GET);
    }

    public Response get(String url, Headers headers, Map<String, Object> queryParams) throws IOException {
        return this.processRequest(url + this.processQueryParams(queryParams), (RequestBody) null, headers, RequestMethod.GET);
    }

    public Response get(Headers headers, Map<String, Object> queryParams) throws IOException {
        return this.processRequest(this.host + this.processQueryParams(queryParams), (RequestBody) null, headers, RequestMethod.GET);
    }

    public Response put(String url, Object payload, Headers headers) throws IOException {
        return this.processRequest(url, payload, headers, RequestMethod.PUT);
    }

    public Response put(Object payload, Headers headers) throws IOException {
        return this.processRequest(this.host, payload, headers, RequestMethod.PUT);
    }

    public Response delete(String url, Object payload, Headers headers) throws IOException {
        return this.processRequest(url, payload, headers, RequestMethod.DELETE);
    }

    private Response processRequest(String url, RequestBody body, Headers headers, RequestMethod method) throws IOException {
        return this.request(url, body, headers, method);
    }

    private Response processRequest(String url, Object payload, Headers headers, RequestMethod method) throws IOException {
        RequestBody body = this.body(this.processBodyWithMediaType(payload));
        return Objects.isNull(headers) ? this.request(url, body, (Headers) null, method) : this.request(url, body, headers, method);
    }

    private String processBodyWithMediaType(Object payload) throws JsonProcessingException {
        return this.mapper.writeValueAsString(payload);
    }

    private String processQueryParams(Map<String, Object> queryParams) throws UnsupportedEncodingException {
        String query = "";

        Map.Entry entry;
        String value;
        for (Iterator var3 = queryParams.entrySet().iterator(); var3.hasNext(); query = query.concat((String) entry.getKey()).concat("=").concat(URLEncoder.encode(value, "UTF-8")).concat("&")) {
            entry = (Map.Entry) var3.next();
            value = String.valueOf(entry.getValue());
        }

        return "?" + query;
    }

    private Response request(String path, RequestBody body, Headers headers, RequestMethod method) throws IOException {
        return this.request(path, body, headers, method, (Callback) null);
    }

    private Response request(String path, RequestBody body, Headers headers, RequestMethod method, Callback callback) throws IOException {
        String requestUrl = this.host.equals(path) ? this.host : this.host.concat(path);
        Request.Builder requestBuilder = (new Request.Builder()).headers(headers).url(requestUrl);
        String var10002 = LocaleContextHolder.getLocale().getLanguage();
        requestBuilder.addHeader("Accept-Language", var10002 + "-" + LocaleContextHolder.getLocale().getCountry());
        Request request = this.initRequestMethod(requestBuilder, method, body).build();
        if (this.getDebugResponse()) {
            logger.info("HttpRequestUrl: " + requestUrl);
            logger.info("HttpRequestBody: " + body);
        }

        if (callback == null) {
            return this.client.newCall(request).execute();
        } else {
            this.client.newCall(request).enqueue(callback);
            return null;
        }
    }

    private Request.Builder initRequestMethod(Request.Builder request, RequestMethod method, RequestBody body) {
        switch (method) {
            case GET:
                request.get();
                break;
            case PUT:
                request.put(body);
                break;
            case DELETE:
                request.delete(body);
                break;
            default:
                request.post(body);
        }

        return request;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    public void setClient(final OkHttpClient client) {
        this.client = client;
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public void setMapper(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public Boolean getDebugResponse() {
        return this.debugResponse;
    }

    public void setDebugResponse(final Boolean debugResponse) {
        this.debugResponse = debugResponse;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = MediaType.parse(mediaType);
    }

    public static class HttpClientBuilder {
        private OkHttpClient client;
        private ObjectMapper mapper;
        private String host;
        private Boolean debugResponse;
        private MediaType mediaType;

        HttpClientBuilder() {
        }

        public HttpClientBuilder client(final OkHttpClient client) {
            this.client = client;
            return this;
        }

        public HttpClientBuilder mapper(final ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        public HttpClientBuilder host(final String host) {
            this.host = host;
            return this;
        }

        public HttpClientBuilder debugResponse(final Boolean debugResponse) {
            this.debugResponse = debugResponse;
            return this;
        }

        public HttpClientBuilder mediaType(final MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this.client, this.mapper, this.host, this.debugResponse, this.mediaType);
        }

        public String toString() {
            return "HttpClient.HttpClientBuilder(client=" + this.client + ", mapper=" + this.mapper + ", host=" + this.host + ", debugResponse=" + this.debugResponse + ", mediaType=" + this.mediaType + ")";
        }
    }

}

