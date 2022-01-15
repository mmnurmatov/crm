package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Message;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.MessageRepository;
import uz.isd.javagroup.grandcrm.services.modules.MessageService;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public List<Message> findAll() {
        List<Message> messages = (List<Message>) messageRepository.findAll();
        return messages;
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long id) throws RecordNotFoundException {
        Optional<Message> messageOpt = messageRepository.findById(id);
        if (messageOpt.isPresent()) return messageOpt.get();
        else throw new RecordNotFoundException("No Message record exists for id: " + id);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
