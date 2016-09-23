package com.zncm.timepill.data.base.relation;


import java.io.Serializable;
import java.util.List;

public class RelationPager<T> implements Serializable {
    public int count;
    public int page_size;
    public List<T> users;
}
