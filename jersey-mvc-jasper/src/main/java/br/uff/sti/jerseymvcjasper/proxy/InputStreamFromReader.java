/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

/**
 *
 * @author marcos
 */
public class InputStreamFromReader extends InputStream {

    PipedInputStream inPipe;

    public InputStreamFromReader(Reader r, String encoding) {
        inPipe = new PipedInputStream();
        PipedOutputStream osPiped = new PipedOutputStream(inPiped);
        
    }

    @Override
    public int read() throws IOException {
        return inPipe.read();
    }

    @Override
    public int read(byte b[]) throws IOException {
        return inPipe.read(b);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return inPipe.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return inPipe.skip(n);
    }

    @Override
    public int available() throws IOException {
        return inPipe.available();
    }
}
