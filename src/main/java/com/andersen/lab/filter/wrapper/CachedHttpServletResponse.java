package com.andersen.lab.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.PrintWriter;

public class CachedHttpServletResponse extends HttpServletResponseWrapper {
    private CachedServletOutputStream cachedServletOutputStream;
    private PrintWriter cachedWriter;

    public CachedHttpServletResponse(HttpServletResponse response) {
        super(response);
        cachedServletOutputStream = new CachedServletOutputStream();
        cachedWriter = new PrintWriter(cachedServletOutputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return cachedServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return cachedWriter;
    }

    public byte[] getCachedOutput() {
        return cachedServletOutputStream.getCachedOutput();
    }
}
