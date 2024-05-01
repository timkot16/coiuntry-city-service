package com.andersen.lab.filter;

import com.andersen.lab.filter.wrapper.CachedHttpServletRequest;
import com.andersen.lab.filter.wrapper.CachedHttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Slf4j
@WebFilter(filterName = "LoggingServletFilter", urlPatterns = "/*")
public class LoggingServletFilter extends OncePerRequestFilter {

    private static final String MULTIPART_FILE_CONTENT_TYPE = "multipart/form-data";
    private static final String FILE_PART_NAME = "file";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long requestTime = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = request;
        String requestBody;
        String contentType = request.getContentType();

        if (isNotBlank(contentType) && contentType.toLowerCase().startsWith(MULTIPART_FILE_CONTENT_TYPE)) {
            Part requestPart = request.getPart(FILE_PART_NAME);
            requestBody = requestPart != null ? requestPart.getSubmittedFileName() : null;
        } else {
            httpServletRequest = new CachedHttpServletRequest(request);
            requestBody = IOUtils.toString(httpServletRequest.getInputStream(), StandardCharsets.UTF_8)
                    .replaceAll("[\\t\\n\\r\\s]+", "");
        }
        String requestMethod = httpServletRequest.getMethod();
        String requestURI = getURIWithParameters(httpServletRequest);
        log.info("Request: [{} {}]; body [{}]", requestMethod, requestURI, requestBody);

        CachedHttpServletResponse httpServletResponse = new CachedHttpServletResponse(response);
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        String responseBody = new String(httpServletResponse.getCachedOutput(), StandardCharsets.UTF_8);
        Integer responseStatus = httpServletResponse.getStatus();
        log.info("Response: [{} {}]; status: [{}]; time: [{} ms]; body: [{}]", requestMethod, requestURI, responseStatus,
                System.currentTimeMillis() - requestTime, responseBody);

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(httpServletResponse.getCachedOutput());
            outputStream.flush();
        }
    }

    private String getURIWithParameters(HttpServletRequest request) {
        StringBuilder requestURI = new StringBuilder(request.getRequestURI());
        String queryString = request.getQueryString();

        return queryString != null ?
                requestURI.append('?').append(queryString).toString() :
                requestURI.toString();
    }

}