package fm.douban.app.control;

import fm.douban.model.Subject;
import fm.douban.service.SubjectService;
import fm.douban.util.SubjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by pawncs on 2020/10/18.
 */
@RestController
@RequestMapping("/test/subject")
public class SubjectTestControl {
    private final SubjectService subjectService;
    @Autowired
    public SubjectTestControl(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @RequestMapping("/add")
    public Subject testAdd(){
        Subject subject = new Subject();
        subject.setId("0");
        return subjectService.addSubject(subject);
    }

    @RequestMapping("/get")
    public Subject testGet(@RequestParam("id")String id){
        return subjectService.get(id) ;
    }
    @RequestMapping("/getByType")
    public List<Subject> testGetByType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_COLLECTION);
    }
    @RequestMapping("/getAll")
    public List<Subject> testGetAll(){
        return subjectService.getSubjects();
    }
    @RequestMapping("/getBySubType")
    public List<Subject> testGetBySubType(){
        return subjectService.getSubjects(SubjectUtil.TYPE_MHZ,SubjectUtil.TYPE_SUB_ARTIST);
    }
    @RequestMapping("/del")
    public boolean testDelete(){
        return subjectService.delete("0");
    }
    @RequestMapping("/delAll")
    public long testDelete2(){
        return subjectService.deleteAll();
    }
}
