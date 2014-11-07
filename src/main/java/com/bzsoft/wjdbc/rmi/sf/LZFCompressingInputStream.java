package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.InputStream;

import com.ning.compress.lzf.LZFInputStream;

public class LZFCompressingInputStream extends LZFInputStream {

    public LZFCompressingInputStream(final InputStream inputStream) throws IOException {
        super(inputStream);
    }

}
