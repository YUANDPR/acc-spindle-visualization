package com.acc.core.entity;

import com.acc.service.impl.JDYServiceImpl;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JDYUser {
    private String name;
    private String username;
    private Integer status;
    private Integer type;
    private List<Integer> departments;

    public JDYUser(Map<String, Object> userMap) {
        this.setName((String) userMap.get("name"));
        this.setUsername((String) userMap.get("username"));
        this.setStatus((Integer) userMap.get("status"));
        this.setType((Integer) userMap.get("type"));
        this.setDepartments(JDYServiceImpl.castList(userMap.get("departments"), Integer.class));
    }
}
