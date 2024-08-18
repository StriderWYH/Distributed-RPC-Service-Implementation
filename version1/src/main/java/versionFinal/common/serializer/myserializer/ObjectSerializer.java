/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package versionFinal.common.serializer.myserializer;

import java.io.*;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
public class ObjectSerializer implements Serializer{
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream boos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(boos);
            oos.writeObject(obj);
            oos.flush();
            bytes = boos.toByteArray();
            oos.close();
            boos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes, int messageType) {
       Object obj = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
       try{
           ObjectInputStream ois = new ObjectInputStream(bais);
           obj = ois.readObject();
           ois.close();
           bais.close();

       } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
       }
       return obj;
    }

    @Override
    public int getType() {
        return 0;
    }
}
