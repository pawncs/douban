package fm.douban.service.impl;

import fm.douban.model.User;
import fm.douban.param.UserQueryParam;
import fm.douban.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.LongSupplier;

/**
 * Created by pawncs on 2021/2/18.
 */
@Service
public class UserServiceImpl implements UserService {
    MongoTemplate mongoTemplate;
    Logger logger ;
    @Autowired
    public UserServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.logger = Logger.getLogger(UserServiceImpl.class);
    }

    @Override
    public User add(User user) {
        if(user == null){
            logger.warn("添加的用户为空");
            return null;
        }
        user.setGmtCreated(LocalDateTime.now());
        user.setGmtModified(LocalDateTime.now());
        return mongoTemplate.insert(user);
    }

    @Override
    public User get(String id) {
        return mongoTemplate.findById(id,User.class);
    }
    @Override
    public User getUserByLoginName(String name) {
        Query query = new Query(Criteria.where("loginName").is(name));
        return mongoTemplate.findOne(query,User.class);
    }

    @Override
    public Page<User> list(UserQueryParam param) {
        Pageable pageable = PageRequest.of(param.getPageNum(),param.getPageSize());
        Query query = new Query();
        query.with(pageable);
        List<User> list = mongoTemplate.find(query,User.class);
        long count = mongoTemplate.count(query,User.class);

        return PageableExecutionUtils.getPage(list, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return count;
            }
        });
    }

    @Override
    public boolean modify(User user) {
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update updateData = new Update();
        updateData.set("gmtModified",LocalDateTime.now());
        String temp = null;
        if((temp = user.getLoginName()) != null){
            updateData.set("loginName",temp);
        }
        if((temp = user.getMobile())!=null){
            updateData.set("mobile",temp);
        }
        if((temp = user.getPassword())!=null){
            updateData.set("password",temp);
        }
        return mongoTemplate.updateFirst(query,updateData,User.class)
                .getModifiedCount() > 0;
    }

    @Override
    public boolean delete(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query,User.class).getDeletedCount()>0;
    }
}
