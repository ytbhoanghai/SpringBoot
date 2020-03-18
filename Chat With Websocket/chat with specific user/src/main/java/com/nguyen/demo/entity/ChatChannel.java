package com.nguyen.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@TypeAlias("chatChannel")
public class ChatChannel {

    @Id
    private String id;
    private Date createDate;
    private List<String> userNames;
    @DBRef
    private Message messageRecently;

}
