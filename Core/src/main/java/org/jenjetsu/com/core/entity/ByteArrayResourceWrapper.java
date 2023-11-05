package org.jenjetsu.com.core.entity;

import java.util.UUID;

import org.springframework.core.io.ByteArrayResource;

public class ByteArrayResourceWrapper extends ByteArrayResource {

    private final String filename;

    public ByteArrayResourceWrapper(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = (filename != null ? filename : UUID.randomUUID().toString());
    }
    
    public ByteArrayResourceWrapper(byte[] byteArray) {
        super(byteArray);
        this.filename = UUID.randomUUID().toString();
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    public static ByteArrayResourceWrapper billResource(byte[] byteArray) {
        String filename = UUID.randomUUID().toString() + ".bill";
        return new ByteArrayResourceWrapper(byteArray, filename);
    }

    public static ByteArrayResourceWrapper billeResource(byte[] byteArray, String name) {
        if(!name.endsWith(".bill")) {
            name += ".bill";
        }
        return new ByteArrayResourceWrapper(byteArray, name);
    }

    public static ByteArrayResourceWrapper cdrResource(byte[] byteArray) {
        String filename = UUID.randomUUID().toString() + ".cdr";
        return new ByteArrayResourceWrapper(byteArray, filename);
    } 

    public static ByteArrayResourceWrapper cdrResource(byte[] byteArray, String name) {
        if(!name.endsWith(".cdr")) {
            name += ".cdr";
        }
        return new ByteArrayResourceWrapper(byteArray, name);
    }

    public static ByteArrayResourceWrapper cdrPlusResource(byte[] byteArray) {
        String filename = UUID.randomUUID().toString() + ".cdrplus";
        return new ByteArrayResourceWrapper(byteArray, filename);
    } 

    public static ByteArrayResourceWrapper cdrPlusResource(byte[] byteArray, String name) {
        if(!name.endsWith(".cdrplus")) {
            name += ".cdrplus";
        }
        return new ByteArrayResourceWrapper(byteArray, name);
    }
}
