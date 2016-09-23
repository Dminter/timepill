package com.zncm.timepill.data.base.note;


import java.io.Serializable;
import java.util.List;

public class Pager<T>  implements Serializable {
    public int count;
    public int page;
    public int page_size;
    public List<T> diaries;
    public List<T> items;
}
