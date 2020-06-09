package ml.socshared.worker.domain.vk.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class VkGroupResponse {
    UUID systemUserId;
    String groupId;
    String name;
    String adapterID = "vk";
    boolean isSelected;//TODO добавление поля в GateWay - путем запроса к хранилищу
    int membersCount;
    VkGroupType type;
    Date createData;
}