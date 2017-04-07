package com.example.photoselect.popwindow;

import java.io.Serializable;

public class PopMenuModel implements Serializable {
    private String name;
    private String id;
    private MenuPopWindow.MenuStyle style = MenuPopWindow.MenuStyle.NORMAL;

    public PopMenuModel() {

    }

    public PopMenuModel(String name) {
        this.name = name;
    }

    public PopMenuModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public PopMenuModel(String name, MenuPopWindow.MenuStyle style) {
        this.name = name;
        if (style == null) {
            style = MenuPopWindow.MenuStyle.NORMAL;
        }
        this.style = style;
    }

    public PopMenuModel(String name, String id, MenuPopWindow.MenuStyle style) {
        this.name = name;
        this.id = id;
        if (style == null) {
            style = MenuPopWindow.MenuStyle.NORMAL;
        }
        this.style = style;
    }

    public MenuPopWindow.MenuStyle getStyle() {
        return style;
    }

    public void setStyle(MenuPopWindow.MenuStyle style) {
        if (style == null) {
            style = MenuPopWindow.MenuStyle.NORMAL;
        }
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
