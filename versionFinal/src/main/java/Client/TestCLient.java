/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package Client;

import Client.proxy.ClientProxy;
import common.pojo.User;
import common.service.UserService;

/**
 * @Author: StriderWYH
 * @CreateTime: 2024-08-06
 * @Description:
 * @Version: 1.0
 */
public class TestCLient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy clientProxy=new ClientProxy(0);
        UserService proxy = (UserService) clientProxy.getProxy(UserService.class);
        for(int i = 0; i < 120; i++) {
            Integer i1 = i;
            if (i%30==0) {
                Thread.sleep(10000);
            }
            new Thread(()->{
                try{
                    User user = proxy.getUserByUserId(i1);
                    if(user != null){
                        System.out.println("User get from the server="+user.toString());
                    }else{
                        System.out.println("Can't get User from the Server.");
                    }

                    Integer id = proxy.insertUserId(User.builder().id(i1).userName("User" + i1.toString()).sex(true).build());
                    System.out.println("ID of the user inserted to the server"+id);
                } catch (NullPointerException e){
                    System.out.println("user is null");
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
