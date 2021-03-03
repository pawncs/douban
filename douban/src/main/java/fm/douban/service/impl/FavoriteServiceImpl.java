package fm.douban.service.impl;

import fm.douban.model.Favorite;
import fm.douban.model.Subject;
import fm.douban.service.FavoriteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2021/3/1.
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    Logger logger;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public FavoriteServiceImpl(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
        logger = Logger.getLogger(FavoriteServiceImpl.class);
    }


    @Override
    public Favorite add(Favorite fav) {
        fav.setGmtCreated(LocalDateTime.now());
        fav.setGmtModified(LocalDateTime.now());
        return mongoTemplate.insert(fav);

    }

    @Override
    public List<Favorite> list(Favorite favParam) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (favParam== null) {
            logger.error("input favParam is not correct.");
            return null;
        }
        Query condition  = new Query();
        String type = favParam.getType();
        String itemType = favParam.getItemType();
        String id = favParam.getId();
        String userId = favParam.getUserId();
        String itemId = favParam.getItemId();
        if (type != null && !type.equals("")) {
            condition.addCriteria(Criteria.where("type").is(type));
        }
        if (itemType != null && !itemType.equals("")) {
            condition.addCriteria(Criteria.where("itemType").is(itemType));
        }
        if (id != null && !id.equals("")) {
            condition.addCriteria(Criteria.where("id").is(id));
        }
        if (userId != null && !userId.equals("")) {
            condition.addCriteria(Criteria.where("userId").is(userId));
        }
        if (itemId != null && !itemId.equals("")) {
            condition.addCriteria(Criteria.where("itemId").is(itemId));
        }

        return mongoTemplate.find(condition, Favorite.class);
    }

    @Override
    public boolean delete(Favorite favParam) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (favParam== null) {
            logger.error("input favParam is not correct.");
            return false;
        }
        Query condition  = new Query();
        String type = favParam.getType();
        String itemType = favParam.getItemType();
        String id = favParam.getId();
        String userId = favParam.getUserId();
        String itemId = favParam.getItemId();
        if (type != null && !type.equals("")) {
            condition.addCriteria(Criteria.where("type").is(type));
        }
        if (itemType != null && !itemType.equals("")) {
            condition.addCriteria(Criteria.where("itemType").is(itemType));
        }
        if (id != null && !id.equals("")) {
            condition.addCriteria(Criteria.where("id").is(id));
        }
        if (userId != null && !userId.equals("")) {
            condition.addCriteria(Criteria.where("userId").is(userId));
        }
        if (itemId != null && !itemId.equals("")) {
            condition.addCriteria(Criteria.where("itemId").is(itemId));
        }

        return mongoTemplate.remove(condition,Favorite.class).getDeletedCount()>0;
    }
}
