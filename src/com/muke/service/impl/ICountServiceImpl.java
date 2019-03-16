package com.muke.service.impl;

import com.muke.dao.ICountDao;
import com.muke.dao.impl.CountDaoImpl;
import com.muke.service.ICountService;

public class ICountServiceImpl implements ICountService {
    private ICountDao countdao = new CountDaoImpl();

    @Override
    public int updateReplyCount(int msgid) {
        return countdao.updateReplyCount(msgid);
    }

}
