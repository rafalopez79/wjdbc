package com.bzsoft.wjdbc.rmi.sf;

import java.io.OutputStream;

import com.ning.compress.lzf.LZFOutputStream;

/**
 * Workaround für kaputten GZipOutputStream, von http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4206909
 * (23-JUN-2002, rsaddey)
 * 
 * @see DecompressingInputStream
 */

public class LZFCompressingOutputStream extends LZFOutputStream {

    /**
     * 
     * @param outputStream
     */
    public LZFCompressingOutputStream(final OutputStream outputStream) {
        super(outputStream);
    }

}
