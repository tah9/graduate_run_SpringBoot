package com.tah.graduate_run.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatMsg implements Serializable {
    private static long serialVersionUID = -8985545025228238754L;
    private String sendUserid;
    private String receivedUserid;
    private Date sendTime;
    private String text;
}
