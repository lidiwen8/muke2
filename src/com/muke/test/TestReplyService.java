package com.muke.test;

import com.muke.service.IReplyService;
import com.muke.service.impl.IReplyServiceImpl;
import com.muke.util.StrUtil;

import java.util.List;
import java.util.Map;

public class TestReplyService {
    private static IReplyService iReplyService = new IReplyServiceImpl();
    public static void main(String[] args) throws Exception {
        List emailList= (List) iReplyService.getReplyUseremail(86,18);
        for (int i = 0; i < emailList.size(); i++) {
            System.out.println(emailList.get(i));
        }
    }
}
