package com.omnigon.aem.sling.mvp.model.presenter;

import java.util.Map;

public interface BeanToMapSerializer {

    Map<String, Object> convertToMap(Object bean);

}
