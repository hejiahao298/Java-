package com.hjh.entity;

/**
 * 书籍的分类
 */
public class Bookcase {
    private Integer id;
    private String name;

    public Bookcase() {
    }

    public Bookcase(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bookcase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
