package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.MessageType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.MessageTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.MessageTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class MessageTypeServiceImpl implements MessageTypeService {

    @Autowired
    MessageTypeRepository messageTypeRepository;

    @Override
    public List<MessageType> findAll() {
        List<MessageType> messageTypes = (List<MessageType>) messageTypeRepository.findAll();
        return messageTypes;
    }

    @Override
    public void saveMessageType(MessageType messageType) {
        messageTypeRepository.save(messageType);
    }

    @Override
    public MessageType getMessageTypeById(Long id) throws RecordNotFoundException {
        Optional<MessageType> messageTypeOpt = messageTypeRepository.findById(id);
        if (messageTypeOpt.isPresent()) return messageTypeOpt.get();
        else throw new RecordNotFoundException("No messageType record exists for given id: " + id);
    }

    @Override
    public void deleteMessageType(Long id) {
        messageTypeRepository.deleteById(id);
    }
}
