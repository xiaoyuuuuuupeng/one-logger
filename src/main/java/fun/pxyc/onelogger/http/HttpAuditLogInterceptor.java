package fun.pxyc.onelogger.http;

import fun.pxyc.onelogger.auditlog.AsyncAuditLog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class HttpAuditLogInterceptor implements ClientHttpRequestInterceptor {
    public HttpAuditLogInterceptor() {}

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        HttpCallInfo callInfo = HttpCallInfo.instance.get();
        if (callInfo == null) {
            return execution.execute(request, body);
        } else {
            callInfo.uri = request.getURI();
            if (request.getMethod() != null) {
                callInfo.method = request.getMethod().toString();
            }
            callInfo.reqHeaders = request.getHeaders().toSingleValueMap();
            if (AsyncAuditLog.isHttpEnabled()) {
                callInfo.reqBodyStr = this.getReqBodyStr(request, body);
            }

            ClientHttpResponse res = execution.execute(request, body);
            callInfo.httpCode = res.getStatusCode().value();
            callInfo.resHeaders = res.getHeaders().toSingleValueMap();
            byte[] resBody = this.readResBody(res);
            callInfo.resBodyStr = this.getResBodyStr(res.getHeaders(), resBody);
            return new SimpleRestClientHttpResponse(res.getStatusCode(), res.getHeaders(), resBody);
        }
    }

    byte[] readResBody(ClientHttpResponse res) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamUtils.copy(res.getBody(), os);
        res.close();
        return os.toByteArray();
    }

    private String getResBodyStr(HttpHeaders headers, byte[] body) {
        if (body != null && body.length > 0 && headers.getContentType() != null) {
            String contentType = headers.getContentType().toString().toLowerCase();
            String charset = !contentType.contains("gbk") && !contentType.contains("gb2312") ? "utf-8" : "gbk";
            if (contentType.contains("application/x-www-form-urlencoded")
                    || contentType.contains("application/json")
                    || contentType.contains("text/xml")
                    || contentType.contains("text/html")
                    || contentType.contains("text/plain")) {
                return this.bodyToString(body, charset);
            }

            byte firstChar = body[0];
            if (firstChar == 60 || firstChar == 123) {
                return this.bodyToString(body, charset);
            }
        }

        return "";
    }

    private String getReqBodyStr(HttpRequest request, byte[] body) {
        if (body != null && request.getHeaders().getContentType() != null) {
            String contentType =
                    request.getHeaders().getContentType().toString().toLowerCase();
            if (contentType.contains("application/x-www-form-urlencoded")
                    || contentType.contains("application/json")
                    || contentType.contains("text/xml")) {
                String charset = !contentType.contains("gbk") && !contentType.contains("gb2312") ? "utf-8" : "gbk";
                return this.bodyToString(body, charset);
            }
        }

        return "";
    }

    private String bodyToString(byte[] body, String charset) {
        try {
            return new String(body, charset);
        } catch (Exception e) {
            return new String(body);
        }
    }

    static class SimpleRestClientHttpResponse implements ClientHttpResponse {
        HttpStatus statusCode;
        HttpHeaders httpHeaders;
        ByteArrayInputStream body;

        SimpleRestClientHttpResponse(HttpStatus statusCode, HttpHeaders httpHeaders, byte[] body) {
            this.statusCode = statusCode;
            this.httpHeaders = httpHeaders;
            this.body = new ByteArrayInputStream(body);
        }

        public HttpStatus getStatusCode() throws IOException {
            return this.statusCode;
        }

        public int getRawStatusCode() throws IOException {
            return this.statusCode.value();
        }

        public String getStatusText() throws IOException {
            return this.statusCode.getReasonPhrase();
        }

        public void close() {
            try {
                this.body.close();
            } catch (Exception e) {
            }
        }

        public InputStream getBody() throws IOException {
            return this.body;
        }

        public HttpHeaders getHeaders() {
            return this.httpHeaders;
        }
    }
}
