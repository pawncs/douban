package fm.douban.service;

import fm.douban.model.Subject;

import java.util.List;

/**
 * Created by pawncs on 2020/10/18.
 */
public interface SubjectService {
    Subject addSubject(Subject subject);
    Subject get(String subjectId);
    List<Subject> getSubjects(String type);
    List<Subject> getSubjects();
    List<Subject> getSubjects(String type,String subType);
    boolean modifySongList(Subject subject);
    boolean delete(String subjectId);
    //删除所有
    long deleteAll();
}
