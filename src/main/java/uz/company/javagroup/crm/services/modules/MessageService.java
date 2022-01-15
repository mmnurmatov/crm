package uz.isd.javagroup.grandcrm.services.modules;

import uz.isd.javagroup.grandcrm.entity.modules.Message;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface MessageService {

    List<Message> findAll();

    void saveMessage(Message message);

    Message getMessageById(Long id) throws RecordNotFoundException;

    void deleteMessage(Long id);
}
