package uz.isd.javagroup.grandcrm.services.directories;

import uz.isd.javagroup.grandcrm.entity.directories.MessageType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface MessageTypeService {

    List<MessageType> findAll();

    void saveMessageType(MessageType messageType);

    MessageType getMessageTypeById(Long id) throws RecordNotFoundException;

    void deleteMessageType(Long id);
}
