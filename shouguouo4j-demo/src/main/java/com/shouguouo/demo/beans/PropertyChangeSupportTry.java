package com.shouguouo.demo.beans;

import com.shouguouo.common.util.OutputUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author shouguouo
 * @date 2022-04-19 20:46:07
 */
public class PropertyChangeSupportTry {

    public static void main(String[] args) {
        Bean bean = new Bean();
        bean.addPropertyChangeListener(evt -> OutputUtils.printf("%s old value: %s new value: %s", evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));
        bean.setId(1000);
        bean.setName("张三");
        bean.clearPropertyChangeListeners();
        bean.setId(1001);
        bean.setName("李四");
    }

    static class Bean {

        private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        private int id;

        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            propertyChangeSupport.firePropertyChange("id", this.getId(), id);
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            propertyChangeSupport.firePropertyChange("name", this.getName(), name);
            this.name = name;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void clearPropertyChangeListeners() {
            for (PropertyChangeListener propertyChangeListener : propertyChangeSupport.getPropertyChangeListeners()) {
                propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
            }
        }
    }
}
