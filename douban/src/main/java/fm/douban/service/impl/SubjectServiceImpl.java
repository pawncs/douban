package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Singer;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2020/10/18.
 */
@Service
public class SubjectServiceImpl implements SubjectService {
    MongoTemplate mongoTemplate;
    SubjectUtil subjectUtil;
    Logger logger;

    @Autowired
    public SubjectServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        subjectUtil = new SubjectUtil();
        logger = Logger.getLogger(SubjectServiceImpl.class);
    }

    @Override
    public Subject addSubject(Subject subject) {
        subject.setGmtCreated(LocalDateTime.now());
        subject.setGmtModified(LocalDateTime.now());
        System.out.println(subject.toString());
        return mongoTemplate.insert(subject);
    }

    @Override
    public Subject get(String subjectId) {
        return mongoTemplate.findById(subjectId, Subject.class);
    }

    @Override
    public List<Subject> getSubjects() {
        return mongoTemplate.findAll(Subject.class);
    }

    @Override
    public List<Subject> getSubjects(Subject subjectParam) {
        // 作为服务，要对入参进行判断，不能假设被调用时，入参一定正确
        if (subjectParam == null) {
            logger.error("input subjectParam is not correct.");
            return null;
        }
//        Criteria criteria = new Criteria();
//        String type = subjectParam.getSubjectType();
//        String subType = subjectParam.getSubjectSubType();
//        String id = subjectParam.getId();
//        String master = subjectParam.getMaster();
//        String name = subjectParam.getName();
//        //todo 多条件查询不能用多个and
//        if (type != null && !type.equals("")) {
//            criteria.andOperator(Criteria.where("subjectType").is(type));
//        }
//        if (subType != null && !subType.equals("")) {
//            criteria.andOperator(Criteria.where("subjectSubType").is(subType));
//        }
//        if (id != null && !id.equals("")) {
//            criteria.andOperator(Criteria.where("id").is(id));
//        }
//        if (master != null && !master.equals("")) {
//            criteria.andOperator(Criteria.where("master").is(master));
//        }
//        if (name != null && !name.equals("")) {
//            criteria.andOperator(Criteria.where("name").is(name));
//        }
//
//
//        Query query = new Query(criteria);
//        return mongoTemplate.find(query, Subject.class);
        Query condition = new Query();
        String type = subjectParam.getSubjectType();
        String subType = subjectParam.getSubjectSubType();
        String id = subjectParam.getId();
        String master = subjectParam.getMaster();
        String name = subjectParam.getName();
        if (type != null && !type.equals("")) {
            condition.addCriteria(Criteria.where("subjectType").is(type));
        }
        if (subType != null && !subType.equals("")) {
            condition.addCriteria(Criteria.where("subjectSubType").is(subType));
        }
        if (id != null && !id.equals("")) {
            condition.addCriteria(Criteria.where("id").is(id));
        }
        if (master != null && !master.equals("")) {
            condition.addCriteria(Criteria.where("master").is(master));
        }
        if (name != null && !name.equals("")) {
            condition.addCriteria(Criteria.where("name").is(name));
        }


        return mongoTemplate.find(condition, Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type) {
        Query query = new Query(Criteria.where("subjectType").is(type));
        return mongoTemplate.find(query, Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type, String subType) {
//        Criteria criteria = new Criteria();
//        criteria.andOperator(Criteria.where("subjectType").is(type),
//                Criteria.where("subjectSubType").is(subType));
//        Query query = new Query(criteria);
//        return mongoTemplate.find(query, Subject.class);
            //代码优化
        Subject subjectParam = new Subject();
        subjectParam.setSubjectType(type);
        subjectParam.setSubjectSubType(subType);
        return getSubjects(subjectParam);
    }

    @Override
    public boolean delete(String subjectId) {
        Subject subject = new Subject();
        subject.setId(subjectId);
        return mongoTemplate.remove(subject).getDeletedCount() > 0;
    }

    @Override
    public long deleteAll() {
        Query query = new Query();
        DeleteResult result = mongoTemplate.remove(query, Subject.class);
        return result.getDeletedCount();
    }

    @Override
    public boolean modifySongList(Subject subject) {
        Query query = new Query(Criteria.where("id").is(subject.getId()));
        Update update = new Update();
        update.set("gmtModified", LocalDateTime.now());
        update.set("songIds", subject.getSongIds());
        return mongoTemplate.updateFirst(query, update, Subject.class)
                .getModifiedCount() >= 1;
    }
}
