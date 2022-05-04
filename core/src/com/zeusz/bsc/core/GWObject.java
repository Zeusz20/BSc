package com.zeusz.bsc.core;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;


public abstract class GWObject implements Serializable {

    /* To generate serialVersionUID, the class's name was
     * fed through a JenkinsOneAtATime hashing algorithm. */

    private static final long serialVersionUID = 1013286201L;

    public final byte[] hash() {
        try(ByteArrayOutputStream blob = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(blob)) {

            // get bytes
            oos.writeObject(this);
            byte[] bytes = blob.toByteArray();

            // hash
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            return md.digest();
        }
        catch(Exception e) {
            return new byte[0];
        }
    }

}
