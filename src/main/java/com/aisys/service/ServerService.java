package com.aisys.service;

import com.aisys.entity.Server;

import java.util.List;

public interface ServerService {

    List<Server> listServer();

    List<Server> listServerByUserId(Integer userId);

    Server getServerById(Integer id);

    Server getDefaultServerByUserId(Integer id);

    Server getServerByName(String name);

    Server getServerByIp(String ip);

    Server getServerByNameOrIp(String ip);

    void addServer(Server server);
    void addDefaultServer(Server server);

    void deleteServer(Integer id);

    void setDefaultServer(Server server);

    void updateServer(Server server);

}
