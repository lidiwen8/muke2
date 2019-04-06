package com.muke.service;

public interface ICountService {

    int updateReplyCount(int msgid);

    int getReplyCount(int msgid);
}
