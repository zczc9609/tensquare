package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //重复添加好友，返回0
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend != null){
            return 0;
        }
        //正常添加，userid->friendid，设置islike为0（单向喜欢）
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);
        //检查friendid->userid，若存在，则两行记录均修改islike为1（互相喜欢）
        if(friendDao.findByUseridAndFriendid(friendid, userid) != null){
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(noFriend != null){
            return 0;
        }
        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    public void deleteFriend(String userid, String friendid) {
        //删除好友表userid->friendid这条记录
        friendDao.deleteFriend(userid,friendid);
        //修改friendid->userid的islike为0
        friendDao.updateIslike("0",userid,friendid);
        //增加非好友表
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
