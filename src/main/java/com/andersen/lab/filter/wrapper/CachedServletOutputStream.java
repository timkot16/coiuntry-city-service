package com.andersen.lab.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;

public class CachedServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream cachedOutputStream;

    public CachedServletOutputStream() {
        cachedOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) {
        cachedOutputStream.write(b);
    }

    public byte[] getCachedOutput() {
        return cachedOutputStream.toByteArray();
    }
}
