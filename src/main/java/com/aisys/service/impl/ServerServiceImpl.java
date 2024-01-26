package com.aisys.service.impl;

import com.aisys.entity.Server;
import com.aisys.mapper.ServerMapper;
import com.aisys.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {
    @Autowired
    private ServerMapper serverMapper;

    @Override
    public List<Server> listServer() {
        return serverMapper.listServer();
    }

    @Override
    public List<Server> listServerByUserId(Integer userId) {
        return serverMapper.listServerByUserId(userId);
    }

    @Override
    public Server getServerById(Integer id) {
        return serverMapper.getServerById(id);
    }

    public Server getDefaultServerByUserId(Integer id) {
        return serverMapper.getDefaultServerByUserId(id);
    }

    @Override
    public Server getServerByName(String name) {
        return serverMapper.getServerByName(name);
    }

    @Override
    public Server getServerByIp(String ip) {
        return serverMapper.getServerByIp(ip);
    }

    @Override
    public Server getServerByNameOrIp(String ip) {
        return serverMapper.getServerByNameOrIp(ip);
    }

    @Override
    public void addServer(Server server) {
        serverMapper.insert(server);
    }

    @Override
    public void addDefaultServer(Server server) {
        serverMapper.insertDefault(server);
    }

    @Override
    public void setDefaultServer(Server server) { serverMapper.setDefaultServer(server); }

    @Override
    public void deleteServer(Integer id) {
        serverMapper.deleteById(id);
    }

    @Override
    public void updateServer(Server server) {
        serverMapper.update(server);
    }
}
