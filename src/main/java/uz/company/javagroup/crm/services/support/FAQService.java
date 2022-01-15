package uz.isd.javagroup.grandcrm.services.support;

import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.support.FAQ;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface FAQService {

    List<FAQ> findAll();

    void saveFAQ(FAQ faq);

    FAQ getFAQById(Long id) throws RecordNotFoundException;

    void deleteFAQ(Long id);

}
