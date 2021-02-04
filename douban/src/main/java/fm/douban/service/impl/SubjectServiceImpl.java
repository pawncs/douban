package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pawncs on 2020/10/18.
 */
@Controller
public class SubjectServiceImpl implements SubjectService {
    MongoTemplate mongoTemplate;
    SubjectUtil subjectUtil;
    @Autowired
    public SubjectServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
        subjectUtil = new SubjectUtil();
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
        return mongoTemplate.findById(subjectId,Subject.class);
    }

    @Override
    public List<Subject> getSubjects() {
        return mongoTemplate.findAll(Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type) {
        Query query = new Query(Criteria.where("subjectType").is(type));
        return mongoTemplate.find(query,Subject.class);
    }

    @Override
    public List<Subject> getSubjects(String type, String subType) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("subjectType").is(type),
                Criteria.where("subjectSubType").is(subType));
        Query query = new Query(criteria);
        return mongoTemplate.find(query,Subject.class);
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
        return  result.getDeletedCount();
    }

    @Override
    public boolean modifySongList(Subject subject) {
        Query query = new Query(Criteria.where("id").is(subject.getId()));
        Update update = new Update();
        update.set("gmtModified",LocalDateTime.now());
        update.set("songIds",subject.getSongIds());
        return mongoTemplate.updateFirst(query,update,Subject.class)
                .getModifiedCount() >= 1;
    }
}
