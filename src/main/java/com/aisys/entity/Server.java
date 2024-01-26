package com.aisys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jack
 */
@Data
public class Server implements Serializable{

    private static final long serialVersionUID = 6576152475207840039L;

    private Integer id;
    private String name;
    private String ip;
    private Integer port;
    private String password;
    private Integer userId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public Integer getUserId() {
        return userId;
    }
}