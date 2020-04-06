package life.majiang.community.service;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.enums.NotificationStatusEnum;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.NotificationMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<NotificationDTO>();

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");
        Integer totalcount = (int)notificationMapper.countByExample(notificationExample);
        Integer  totalpage;
        if(totalcount%size==0){
            totalpage=totalcount/size;
        }else {
            totalpage=totalcount/size+1;
        }

        if(page>totalpage){
            page=totalpage;
        } if(page<1){
            page=1;
        }
        paginationDTO.setpaginationDTO(totalpage,page,size);
        Integer offset=size*(paginationDTO.getPage()-1);


        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
        if(notifications.size()==0){
            return  paginationDTO;
        }
        List<NotificationDTO> notificationDTOS=new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
       
    }

    public long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
      return   notificationMapper.countByExample(notificationExample);

    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
        throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(), user.getId())){
        throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());

        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
