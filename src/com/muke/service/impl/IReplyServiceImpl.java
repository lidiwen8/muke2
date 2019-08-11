package com.muke.service.impl;

import com.muke.dao.IReplyDao;
import com.muke.dao.impl.ReplyDaoImpl;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.Reply;
import com.muke.service.IReplyService;
import com.muke.util.Page;

import java.util.Date;
import java.util.List;

public class IReplyServiceImpl implements IReplyService {

    private IReplyDao replydao = new ReplyDaoImpl();

    @Override
    public int replyMsg(Reply reply) {
        return replydao.replyMsg(reply);
    }

    @Override
    public Page getReply(int msgid, Page page) {
        return replydao.queryBymsgid(msgid, page);
    }

    @Override
    public Page getAuthorReply(int msgid, int userid, Page page){
        return replydao.getAuthorReply(msgid,userid, page);
    }

    @Override
    public long queryReplyConutBymsgid(int msgid) {
        return replydao.queryReplyConutBymsgid(msgid);
    }
    @Override
    public long queryAutherReplyConutBymsgid(int msgid,int userid){
        return replydao.queryAutherReplyConutBymsgid(msgid,userid);
    }

    @Override
    public long queryReplyConutInTotalByreplytime(int msgid,Date replytime){
        return replydao.queryReplyConutInTotalByreplytime(msgid,replytime);
    }

    @Override
    public long queryReplyCountByDate(Date startDate, Date endDate) {
        return replydao.queryCountByDate(startDate, endDate);
    }

    @Override
    public long queryReplyCountBy() {
        return replydao.queryCountBy();
    }

    @Override
    public long queryVisit() {
        return replydao.queryVisit();
    }

    @Override
    public Reply queryid(int replyid) {
        return replydao.queryid(replyid);
    }

    @Override
    public int updateReply(Reply reply) {
        return replydao.updateReply(reply);
    }

    @Override
    public int updateReply2(Reply reply) {
        return replydao.updateReply2(reply);
    }

    @Override
    public List getReplylikeUserid(int replyid) {
        return replydao.getReplylikeUserid(replyid);
    }

    @Override
    public int updateReplylike(Reply reply) {
        return replydao.updateReplylike(reply);
    }

    @Override
    public int updateReplyState(int replyid, int rstate) {
        return replydao.updateReplyState(replyid, rstate);
    }

    @Override
    public int deleteReply(int replyid) {
        return replydao.deleteReply(replyid);
    }

    @Override
    public List getReplyUseremail(int msgid,int userid){
        return replydao.getReplyUseremail(msgid,userid);
    }

    @Override
    public List AdmingetReplyUseremail(int msgid){
        return replydao.AdmingetReplyUseremail(msgid);
    }

    @Override
    public List AdmingetMsgUseremail(int msgid){
        return replydao.AdmingetMsgUseremail(msgid);
    }

}
