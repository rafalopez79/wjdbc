package com.bzsoft.wjdbc.rmi.sf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Workaround für kaputten GZipOutputStream, von
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4206909 (23-JUN-2002,
 * rsaddey)
 * 
 * @see DecompressingInputStream
 */
/**
 * Now this is tricky: We force the Deflater to flush its data by switching compression level. As yet, a perplexingly
 * simple workaround for http://developer.java.sun.com/developer/bugParade/bugs/4255743.html
 */
public class CompressingOutputStream extends DeflaterOutputStream {

    private static final byte[] EMPTYBYTEARRAY = new byte[0];

    private final int compressionMode;

    public CompressingOutputStream(final OutputStream out, final int compressionMode) {
        super(out, new Deflater(compressionMode, true), 1024 * 1024);
        this.compressionMode = compressionMode;
    }

    /**
     * Insure all remaining data will be output.
     */
    @Override
    public void flush() throws IOException {
        def.setInput(EMPTYBYTEARRAY, 0, 0);
        def.setLevel(Deflater.NO_COMPRESSION);
        deflate();
        def.setLevel(compressionMode);
        deflate();
        super.flush();
    }

    @Override
    protected void deflate() throws IOException {
        int len = 0;
        do {
            len = def.deflate(buf, 0, buf.length);
            if (len > 0) {
                out.write(buf, 0, len);
            }
        } while (len > 0);
    }

    @Override
    public void close() throws IOException {
        flush();
        super.close();
        def.end();
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        super.write(b, off, len);
    }
}
