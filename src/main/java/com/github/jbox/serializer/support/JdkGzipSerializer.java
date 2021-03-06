package com.github.jbox.serializer.support;

import com.github.jbox.serializer.AbstractSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author jifang
 * @since 2016/7/2 下午5:00.
 */
public class JdkGzipSerializer extends AbstractSerializer {

    @Override
    protected byte[] doSerialize(Object obj) throws Throwable {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzout = new GZIPOutputStream(bos);
             ObjectOutputStream out = new ObjectOutputStream(gzout)) {

            out.writeObject(obj);
            return bos.toByteArray();
        }
    }

    @Override
    protected Object doDeserialize(byte[] bytes) throws Throwable {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             GZIPInputStream gzin = new GZIPInputStream(bis);
             ObjectInputStream ois = new ObjectInputStream(gzin)) {

            return ois.readObject();
        }
    }
}