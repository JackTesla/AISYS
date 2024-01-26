package com.aisys.service.impl;

import com.aisys.entity.Menu;
import com.aisys.mapper.MenuMapper;
import com.aisys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jack
 */
@Service
public class MenuServiceImpl implements MenuService {


    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> listMenu() {
        List<Menu> menuList = menuMapper.listMenu();
        return menuList;
    }

    @Override
    public Menu insertMenu(Menu menu) {
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    public void deleteMenu(Integer id) {
        menuMapper.deleteById(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        menuMapper.update(menu);
    }

    @Override
    public Menu getMenuById(Integer id) {
        return menuMapper.getMenuById(id);
    }
}
