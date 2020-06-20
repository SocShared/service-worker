package ml.socshared.worker.domain.adapter.response;

import lombok.Data;
import ml.socshared.worker.domain.vk.response.VkGroupType;

import java.util.Date;
import java.util.UUID;

@Data
public class AdapterGroupResponse {
    UUID systemUserId;
    String groupId;
    String name;
    String adapterID ;
    boolean isSelected;//TODO добавление поля в GateWay - путем запроса к хранилищу
    int membersCount;
    String type;
    Date createData;
}