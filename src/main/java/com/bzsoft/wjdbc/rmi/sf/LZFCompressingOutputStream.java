package com.bzsoft.wjdbc.rmi.sf;

import java.io.OutputStream;

import com.ning.compress.lzf.LZFOutputStream;

public class LZFCompressingOutputStream extends LZFOutputStream {

	public LZFCompressingOutputStream(final OutputStream os) {
		super(os);
	}

}
