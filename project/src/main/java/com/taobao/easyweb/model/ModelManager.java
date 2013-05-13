package com.taobao.easyweb.model;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Date;

/**
 * User: jimmey/shantong
 * DateTime: 13-4-15 ÏÂÎç2:43
 */
@Component
public class ModelManager {

//    @Resource
//    private Dao dao;
//
//    public void saveModel(Model model) {
//        if (model.getId() == null) {
//            model.setGmtCreate(new Date());
//            model.setGmtModified(new Date());
//            dao.insert(model);
//        } else {
//            model.setGmtModified(new Date());
//            dao.update(model);
//        }
//    }
}
