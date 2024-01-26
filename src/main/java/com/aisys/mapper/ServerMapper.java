package com.aisys.mapper;

import com.aisys.entity.Server;

import java.util.List;

public interface ServerMapper {
    List<Server> listServer();

    List<Server> listServerByUserId(Integer userId);

    Server getServerById(Integer id);

    Server getDefaultServerByUserId(Integer id);

    Server getServerByName(String name);

    Server getServerByIp(String ip);

    Server getServerByNameOrIp(String ip);

    int insert(Server server);
    int insertDefault(Server server);

    int deleteById(Integer id);

    int setDefaultServer(Server server);

    int update(Server server);

}
