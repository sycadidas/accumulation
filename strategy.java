


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * <class.name>:ComponentOpManagement
 * 使用一句话介绍类的作用:
 * 组件操作管理类
 * @author sunyanchen
 * @version 0.0.1
 * @create 2019-08-01 17:39
 **/
@Component
public class ComponentOpManagement implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ComponentOpManagement.class);

    private Map<ComponentEnum, ComponentOperation> componentServiceMap = new HashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String, ComponentOperation> zkConfigHolders = applicationContext.getBeansOfType(ComponentOperation.class);
        if(!CollectionUtils.isEmpty(zkConfigHolders)){
            for (Map.Entry<String, ComponentOperation> entry : zkConfigHolders.entrySet()) {
                ComponentOperation operationService = entry.getValue();
                Class<? extends ComponentOperation> configClass = operationService.getClass();
                if(configClass.isAnnotationPresent(ComponentService.class)){
                    ComponentService annotation = configClass.getAnnotation(ComponentService.class);
                    ComponentEnum componentEnum = annotation.componentEnum();
                    componentServiceMap.put(componentEnum, operationService);
                }
            }
        }
    }

    /**
     * 获取服务
     */
    public ComponentOperation getServiceByEnum(ComponentEnum cenum){
        if(cenum==null){
            log.error("入参枚举为空！");
            return null;
        }
        ComponentOperation operation = componentServiceMap.get(cenum);
        return operation;
    }
}
