package fm.douban.service.impl;

import com.mongodb.client.result.DeleteResult;
import fm.douban.model.Song;
import fm.douban.param.SongQueryParam;
import fm.douban.service.SongService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

/**
 * Created by pawncs on 2020/10/15.
 */
@Service
public class SongServiceImpl implements SongService {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public SongServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Song add(Song song) {
        song.setGmtCreated(LocalDateTime.now());
        song.setGmtModified(LocalDateTime.now());
        return mongoTemplate.insert(song);
    }

    @Override
    public Song get(String songId) {
        return mongoTemplate.findById(songId, Song.class);
    }

    @Override
    public Page<Song> list(SongQueryParam songParam) {

        Pageable pageable = PageRequest.of(songParam.getPageNum(),songParam.getPageSize());
        List<Criteria> criteriaList = new ArrayList<>();
        Criteria allCriteria = null;
        if(songParam.getName()!=null){
            Criteria criteria = Criteria.where("name").is(songParam.getName());
            criteriaList.add(criteria);
        }
        if(songParam.getLyrics()!=null){
            Criteria criteria = Criteria.where("lyrics").is(songParam.getLyrics());
            criteriaList.add(criteria);
        }
        if(criteriaList.size() == 1){
            allCriteria = criteriaList.get(0);
        }else if(criteriaList.size() > 1){
            allCriteria = new Criteria().andOperator(criteriaList.get(0),criteriaList.get(1));
        }

        Query query ;
        query = allCriteria==null ? new Query(): new Query(allCriteria);
        query.with(pageable);
        List<Song> songs = mongoTemplate.find(query, Song.class);
        long count = mongoTemplate.count(query, Song.class);
        return PageableExecutionUtils.getPage(songs, pageable, new LongSupplier() {
            @Override
            public long getAsLong() {
                return count;
            }
        });
    }

    @Override
    public boolean modify(Song song) {
        Query query = new Query(Criteria.where("id").is(song.getId()));
        Update updateData = new Update();
        updateData.set("gmtModified",LocalDateTime.now());
        if(song.getCover()!=null){
            updateData.set("cover",song.getCover());
        }
        if(song.getLyrics()!=null){
            updateData.set("lyrics",song.getLyrics());
        }
        if(song.getName()!=null){
            updateData.set("name",song.getName());
        }
        if(song.getSingerIds()!=null){
            updateData.set("singerIds",song.getSingerIds());
        }
        if(song.getUrl()!=null){
            updateData.set("url",song.getUrl());
        }
        return mongoTemplate.updateFirst(query,updateData, Song.class)
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean delete(String songId) {
        Query query = new Query(Criteria.where("id").is(songId));
        return mongoTemplate.remove(query, Song.class)
                .getDeletedCount() >= 1;
    }

    @Override
    public long deleteAll() {
        Query query = new Query();
        DeleteResult result = mongoTemplate.remove(query, Song.class);
        return  result.getDeletedCount();
    }
}
