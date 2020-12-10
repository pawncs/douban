package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fm.douban.model.Singer;
import fm.douban.service.SingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2020/10/15.
 */
@Service
public class SingerServiceImpl implements SingerService {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public SingerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Singer addSinger(Singer singer) {
        singer.setGmtCreated(LocalDateTime.now());
        singer.setGmtModified(LocalDateTime.now());
        return mongoTemplate.insert(singer);
    }

    @Override
    public Singer get(String singerId) {
        return mongoTemplate.findById(singerId,Singer.class);
    }

    @Override
    public List<Singer> getAll() {
        return mongoTemplate.findAll(Singer.class);
    }

    @Override
    public boolean modify(Singer singer) {
        Query query = new Query(Criteria.where("id").is(singer.getId()));

        Update updateData = new Update();
        updateData.set("gmtModified",LocalDateTime.now());
        if(singer.getAvatar() != null){
            updateData.set("avatar",singer.getAvatar());
        }
        if(singer.getHomepage() != null){
            updateData.set("homepage",singer.getHomepage());
        }
        if(singer.getName() != null){
            updateData.set("name",singer.getName());
        }
        if(singer.getSimilarSingerIds()!=null){
            updateData.set("similarSingerIds",singer.getSimilarSingerIds());
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(query,updateData,Singer.class);

        return updateResult.getModifiedCount() >= 1;
    }

    @Override
    public boolean delete(String singerId) {
        Singer singer = new Singer();
        singer.setId(singerId);
        DeleteResult result = mongoTemplate.remove(singer);
        return result.getDeletedCount() >= 1;
    }

    @Override
    public long deleteAll() {
        Query query = new Query();
        DeleteResult result = mongoTemplate.remove(query,Singer.class);
        return  result.getDeletedCount();
    }
}
