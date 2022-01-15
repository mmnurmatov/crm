package uz.isd.javagroup.grandcrm.Impl.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.support.FAQ;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.support.FAQRepository;
import uz.isd.javagroup.grandcrm.services.support.FAQService;

import java.util.List;
import java.util.Optional;

@Service
public class FAQServiceImpl implements FAQService {

    @Autowired
    FAQRepository faqRepository;

    @Override
    public List<FAQ> findAll() {
        List<FAQ> faqs = faqRepository.findAll();
        return faqs;
    }

    @Override
    public void saveFAQ(FAQ faq) {

        faqRepository.save(faq);
    }

    @Override
    public FAQ getFAQById(Long id) throws RecordNotFoundException {
        Optional<FAQ> faqOpt = faqRepository.findById(id);
        if (faqOpt.isPresent()) return faqOpt.get();
        else throw new RecordNotFoundException("No FAQ record exists for id: " + id);
    }


    @Override
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }
}
