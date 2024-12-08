package org.mynet.shoppingsite.model;

public class CustomerUpdate {
    public Long id;
    public String username;
    public String password;
    public String email;
    public String address;
    // 无参构造函数
    public CustomerUpdate() {}

    CustomerUpdate(Long id, String username, String password, String email, String address){
        this.id=id;
        this.username=username;
        this.password=password;
        this.email=email;
        this.address=address;
    }
}
